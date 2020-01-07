"""
    Collection of utils
"""
from subprocess import Popen, PIPE
import logging

log = logging.getLogger(__name__)


class Command(object):
    rc = None
    stdout = None
    stderr = None
    cmd = None

    def __init__(self, cmd):
        self.cmd = cmd


def execute(command_str: str) -> Command:
    """
    Executed a command and gets the return code, stdout and stderr

    :param command_str:
    :return:
    """

    command = Command(command_str)

    log.debug("Executing command: " + command.cmd)

    p = Popen(command.cmd.split(" "), stdin=PIPE, stdout=PIPE, stderr=PIPE)
    command.stdout, command.stderr = p.communicate()
    log.debug("Stdout: " + str(command.stdout))
    command.rc = p.returncode

    if command.rc != 0:
        log.error("Unable to execute command: " + command.cmd)
        log.error(str(command.stderr))

    return command
