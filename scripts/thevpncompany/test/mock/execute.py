"""

Mock for execute module


"""


class MockExecute:
    def __init__(self, rc, stdout, stderr):
        self.rc = rc
        self.stdout = stdout
        self.stderr = stderr
