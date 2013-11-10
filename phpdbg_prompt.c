/*
   +----------------------------------------------------------------------+
   | PHP Version 5                                                        |
   +----------------------------------------------------------------------+
   | Copyright (c) 1997-2013 The PHP Group                                |
   +----------------------------------------------------------------------+
   | This source file is subject to version 3.01 of the PHP license,      |
   | that is bundled with this package in the file LICENSE, and is        |
   | available through the world-wide-web at the following url:           |
   | http://www.php.net/license/3_01.txt                                  |
   | If you did not receive a copy of the PHP license and are unable to   |
   | obtain it through the world-wide-web, please send a note to          |
   | license@php.net so we can mail you a copy immediately.               |
   +----------------------------------------------------------------------+
   | Authors: Felipe Pena <felipe@php.net>                                |
   | Authors: Joe Watkins <joe.watkins@live.co.uk>                        |
   +----------------------------------------------------------------------+
*/

#include <stdio.h>
#include <string.h>
#include "zend.h"
#include "zend_compile.h"
#include "phpdbg.h"
#include "phpdbg_help.h"
#include "phpdbg_bp.h"
#include "phpdbg_opcode.h"

static const phpdbg_command_t phpdbg_prompt_commands[];

ZEND_EXTERN_MODULE_GLOBALS(phpdbg);

static PHPDBG_COMMAND(exec) /* {{{ */
{
	if (PHPDBG_G(exec)) {
		printf("Unsetting old execution context: %s\n", PHPDBG_G(exec));
		efree(PHPDBG_G(exec));
		PHPDBG_G(exec) = NULL;
	}

	if (PHPDBG_G(ops)) {
		printf("Destroying compiled opcodes\n");
		destroy_op_array(PHPDBG_G(ops) TSRMLS_CC);
		efree(PHPDBG_G(ops));
		PHPDBG_G(ops) = NULL;
	}

	PHPDBG_G(exec) = estrndup(expr, PHPDBG_G(exec_len) = expr_len);

	printf("Set execution context: %s\n", PHPDBG_G(exec));

	return SUCCESS;
} /* }}} */

static inline int phpdbg_compile(TSRMLS_D) /* {{{ */
{
	zend_file_handle fh;

	if (!EG(in_execution)) {
	    printf("Attempting compilation of %s\n", PHPDBG_G(exec));
        
	    if (php_stream_open_for_zend_ex(PHPDBG_G(exec), &fh,
		        USE_PATH|STREAM_OPEN_FOR_INCLUDE TSRMLS_CC) == SUCCESS) {
		    PHPDBG_G(ops) = zend_compile_file(
		        &fh, ZEND_INCLUDE TSRMLS_CC);
		    zend_destroy_file_handle(&fh TSRMLS_CC);
		    printf("Success\n");
		    return SUCCESS;
        } else {
            printf("Could not open file %s\n", PHPDBG_G(exec));
        }
	} else {
	    printf("Cannot compile while in execution\n");
	}

	return FAILURE;
} /* }}} */

static PHPDBG_COMMAND(compile) /* {{{ */
{
	if (PHPDBG_G(exec)) {
		if (PHPDBG_G(ops)) {
			printf("Destroying compiled opcodes\n");
			destroy_op_array(PHPDBG_G(ops) TSRMLS_CC);
			efree(PHPDBG_G(ops));
			PHPDBG_G(ops)=NULL;
		}

		return phpdbg_compile(TSRMLS_C);
	} else {
		printf("No execution context\n");
		return FAILURE;
	}
} /* }}} */

static PHPDBG_COMMAND(step) /* {{{ */
{
	PHPDBG_G(stepping) = atoi(expr);
	return SUCCESS;
} /* }}} */

static PHPDBG_COMMAND(next) /* {{{ */
{
	return PHPDBG_NEXT;
} /* }}} */

static PHPDBG_COMMAND(run) /* {{{ */
{
	if (PHPDBG_G(ops) || PHPDBG_G(exec)) {
		if (!PHPDBG_G(ops)) {
			if (phpdbg_compile(TSRMLS_C) == FAILURE) {
				printf("Failed to compile %s, cannot run\n", PHPDBG_G(exec));
				return FAILURE;
			}
		}

		EG(active_op_array) = PHPDBG_G(ops);
		EG(return_value_ptr_ptr) = &PHPDBG_G(retval);

		zend_try {
			zend_execute(EG(active_op_array) TSRMLS_CC);
		} zend_catch {
			if (!PHPDBG_G(quitting)) {
				printf("Caught excetion in VM\n");
				return FAILURE;
			} else return SUCCESS;
		} zend_end_try();

		return SUCCESS;
	} else {
		printf("Nothing to execute !\n");
		return FAILURE;
	}
} /* }}} */

static PHPDBG_COMMAND(eval) /* {{{ */
{
	zval retval;

	if (expr) {
		if (zend_eval_stringl((char*)expr, expr_len-1,
			&retval, "eval()'d code" TSRMLS_CC) == SUCCESS) {
			printf("Success: ");
			zend_print_zval_r(&retval, 0 TSRMLS_CC);
			printf("\n");
			zval_dtor(&retval);
		}
	} else {
		printf("No expression provided !\n");
		return FAILURE;
	}

	return SUCCESS;
} /* }}} */

static PHPDBG_COMMAND(back) /* {{{ */
{
	if (!EG(in_execution)) {
		printf("Not executing !\n");
		return FAILURE;
	}
	zval zbacktrace;
	zval **tmp;
	HashPosition position;
	int i = 0, limit = (expr != NULL) ? atoi(expr) : 0;

	zend_fetch_debug_backtrace(&zbacktrace, 0, 0, limit TSRMLS_CC);

	for (zend_hash_internal_pointer_reset_ex(Z_ARRVAL(zbacktrace), &position);
		zend_hash_get_current_data_ex(Z_ARRVAL(zbacktrace), (void**)&tmp, &position) == SUCCESS;
		zend_hash_move_forward_ex(Z_ARRVAL(zbacktrace), &position)) {
		if (i++) {
			printf(",\n");
		}
		zend_print_flat_zval_r(*tmp TSRMLS_CC);
	}

	printf("\n");
	zval_dtor(&zbacktrace);

	return SUCCESS;
} /* }}} */

static PHPDBG_COMMAND(print) /* {{{ */
{
	if (expr_len) {
		printf("%s\n", expr);
		return SUCCESS;
	}

	printf("Showing Execution Context Information:\n");
	printf("Exec\t\t%s\n", PHPDBG_G(exec) ? PHPDBG_G(exec) : "none");
	printf("Compiled\t%s\n", PHPDBG_G(ops) ? "yes" : "no");
	printf("Stepping\t%s\n", PHPDBG_G(stepping) ? "on" : "off");

	if (PHPDBG_G(ops)) {
		printf("Opcodes\t\t%d\n", PHPDBG_G(ops)->last);

		if (PHPDBG_G(ops)->last_var) {
			printf("Variables\t%d\n", PHPDBG_G(ops)->last_var-1);
		} else {
			printf("Variables\tNone\n");
		}
	}
	printf("Executing\t%s\n", EG(in_execution) ? "yes" : "no");

	if (EG(in_execution)) {
		printf("VM Return\t%d\n", PHPDBG_G(vmret));
	}

	return SUCCESS;
} /* }}} */

static PHPDBG_COMMAND(break) /* {{{ */
{
	const char *line_pos = zend_memrchr(expr, ':', expr_len);

	if (line_pos) {
		char path[MAXPATHLEN], resolved_name[MAXPATHLEN];
		long line_num = strtol(line_pos+1, NULL, 0);

		memcpy(path, expr, line_pos - expr);
		path[line_pos - expr] = 0;

		if (expand_filepath(path, resolved_name TSRMLS_CC) == NULL) {
			return FAILURE;
		}

		phpdbg_set_breakpoint_file(resolved_name, line_num TSRMLS_CC);
	} else {
		char name[200];
		size_t name_len = strlen(expr);

		name_len = MIN(name_len, 200);
		memcpy(name, expr, name_len);
		name[name_len] = 0;

		phpdbg_set_breakpoint_symbol(name TSRMLS_CC);
	}

	return SUCCESS;
} /* }}} */

static PHPDBG_COMMAND(quit) /* {{{ */
{
    PHPDBG_G(quitting)=1;

	zend_bailout();

	return SUCCESS;
} /* }}} */

static PHPDBG_COMMAND(help) /* {{{ */
{
	printf("Welcome to phpdbg, the interactive PHP debugger.\n");

	if (!expr_len) {
		const phpdbg_command_t *prompt_command = phpdbg_prompt_commands;
		const phpdbg_command_t *help_command = phpdbg_help_commands;

		printf("To get help regarding a specific command type \"help command\"\n");

		printf("Commands:\n");
		while (prompt_command && prompt_command->name) {
			printf("\t%s\t%s\n", prompt_command->name, prompt_command->tip);
			++prompt_command;
		}

		printf("Helpers Loaded:\n");
		while (help_command && help_command->name) {
			printf("\t%s\t%s\n", help_command->name, help_command->tip);
			++help_command;
		}
	} else {
		if (phpdbg_do_cmd(phpdbg_help_commands, (char*)expr, expr_len TSRMLS_CC) == FAILURE) {
			printf("failed to find help command: %s\n", expr);
		}
	}
	printf("Please report bugs to <https://github.com/krakjoe/phpdbg/issues>\n");

	return SUCCESS;
} /* }}} */

static PHPDBG_COMMAND(quiet) { /* {{{ */
    PHPDBG_G(quiet) = atoi(expr);
    return SUCCESS;
} /* }}} */

static const phpdbg_command_t phpdbg_prompt_commands[] = {
	PHPDBG_COMMAND_D(exec,      "set execution context"),
	PHPDBG_COMMAND_D(compile,   "attempt to pre-compile execution context"),
	PHPDBG_COMMAND_D(step,      "step through execution"),
	PHPDBG_COMMAND_D(next,      "continue execution"),
	PHPDBG_COMMAND_D(run,       "attempt execution"),
	PHPDBG_COMMAND_D(eval,      "evaluate some code"),
	PHPDBG_COMMAND_D(print,     "print something"),
	PHPDBG_COMMAND_D(break,     "set breakpoint"),
	PHPDBG_COMMAND_D(back,      "show backtrace"),
	PHPDBG_COMMAND_D(help,      "show help menu"),
	PHPDBG_COMMAND_D(quiet,     "silence some output"),
	PHPDBG_COMMAND_D(quit,      "exit phpdbg"),
	{NULL, 0, 0}
};

int phpdbg_do_cmd(const phpdbg_command_t *command, char *cmd_line, size_t cmd_len TSRMLS_DC) /* {{{ */
{
	char *params = NULL;
	const char *cmd = strtok_r(cmd_line, " ", &params);
	size_t expr_len = cmd != NULL ? strlen(cmd) : 0;

	while (command && command->name) {
		if (command->name_len == expr_len
			    && memcmp(cmd, command->name, expr_len) == 0) {
			PHPDBG_G(last) = command;
			PHPDBG_G(last_params) = params;
			PHPDBG_G(last_params_len) = cmd_len - expr_len;
			return command->handler(params, cmd_len - expr_len TSRMLS_CC);
		}
		++command;
	}

	return FAILURE;
} /* }}} */

int phpdbg_interactive(int argc, char **argv TSRMLS_DC) /* {{{ */
{
	char cmd[PHPDBG_MAX_CMD];

	printf("phpdbg> ");

	while (!PHPDBG_G(quitting) &&
	       fgets(cmd, PHPDBG_MAX_CMD, stdin) != NULL) {
		size_t cmd_len = strlen(cmd) - 1;

		if (cmd[cmd_len] == '\n') {
			cmd[cmd_len] = 0;
		}

		if (cmd_len) {
		    switch (phpdbg_do_cmd(phpdbg_prompt_commands, cmd, cmd_len TSRMLS_CC)) {
		        case FAILURE:
		            if (!PHPDBG_G(quitting)) {
		                printf(
		                    "Failed to execute %s !\n", cmd);
		            }
		        break;

		        case PHPDBG_NEXT:
		            return PHPDBG_NEXT;

		    }
		} else if (PHPDBG_G(last)) {
		    PHPDBG_G(last)->handler(
		        PHPDBG_G(last_params), PHPDBG_G(last_params_len) TSRMLS_CC);
		}

		if (!PHPDBG_G(quitting)) {
		    printf("phpdbg> ");
		}

	}

	return SUCCESS;
} /* }}} */

static void phpdbg_print_opline(zend_execute_data *execute_data TSRMLS_DC) /* {{{ */
{
    if (!PHPDBG_G(quiet)) {
        zend_op *opline = execute_data->opline;

        printf("[OPLINE: %p:%s]\n", opline, phpdbg_decode_opcode(opline->opcode));
    }
} /* }}} */

void phpdbg_execute_ex(zend_execute_data *execute_data TSRMLS_DC) /* {{{ */
{
	zend_bool original_in_execution = EG(in_execution);

	EG(in_execution) = 1;

	if (0) {
zend_vm_enter:
		execute_data = zend_create_execute_data_from_op_array(EG(active_op_array), 1 TSRMLS_CC);
	}

	while (1) {
#ifdef ZEND_WIN32
		if (EG(timed_out)) {
			zend_timeout(0);
		}
#endif

        if (PHPDBG_G(has_file_bp)
			&& phpdbg_find_breakpoint_file(execute_data->op_array TSRMLS_CC) == SUCCESS) {
			while (phpdbg_interactive(0, NULL TSRMLS_CC) != PHPDBG_NEXT) {
				continue;
			}
		}

        if (PHPDBG_G(has_sym_bp) && execute_data->opline->opcode != ZEND_RETURN) {
            zend_execute_data *previous = execute_data->prev_execute_data;
            if (previous && previous != execute_data && previous->opline) {
				if (previous->opline->opcode == ZEND_DO_FCALL
					|| previous->opline->opcode == ZEND_DO_FCALL_BY_NAME) {
					if (phpdbg_find_breakpoint_symbol(
						previous->function_state.function TSRMLS_CC) == SUCCESS) {
						while (phpdbg_interactive(0, NULL TSRMLS_CC) != PHPDBG_NEXT) {
							continue;
						}
					}
				}
			}
        }

		PHPDBG_G(vmret) = execute_data->opline->handler(execute_data TSRMLS_CC);

		phpdbg_print_opline(
		    execute_data TSRMLS_CC);

		if (PHPDBG_G(stepping)) {
			while (phpdbg_interactive(0, NULL TSRMLS_CC) != PHPDBG_NEXT) {
				continue;
			}
		}

		if (PHPDBG_G(vmret) > 0) {
			switch (PHPDBG_G(vmret)) {
				case 1:
					EG(in_execution) = original_in_execution;
					return;
				case 2:
					goto zend_vm_enter;
					break;
				case 3:
					execute_data = EG(current_execute_data);
					break;
				default:
					break;
			}
		}

	}
	zend_error_noreturn(E_ERROR, "Arrived at end of main loop which shouldn't happen");
} /* }}} */