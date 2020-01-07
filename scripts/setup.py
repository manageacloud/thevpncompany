from setuptools import setup, find_packages
import re
import os
import codecs

with open('requirements.txt') as f:
    install_requires = f.read().splitlines()


def read(*parts):
    path = os.path.join(os.path.dirname(__file__), *parts)
    with codecs.open(path, encoding='utf-8') as fobj:
        return fobj.read()


def find_version(*file_paths):
    version_file = read(*file_paths)
    version_match = re.search(r"^__version__ = ['\"]([^'\"]*)['\"]",
                              version_file, re.M)
    if version_match:
        return version_match.group(1)
    raise RuntimeError('Unable to find version string.')


long_description = open('README.rst').read()

setup(
    # Metadata
    name='thevpncompany',
    version=find_version('thevpncompany', '__init__.py'),
    author='Ruben Rubio Rey',
    author_email='ruben@thevpncompany.com.au',
    url='https://thevpncompany.com.au',
    description='Scripts required for the open source end-to-end solution TheVPNCompany.com.au',
    long_description=long_description,
    long_description_content_type='text/x-rst',
    license='MIT',
    install_requires=install_requires,
    keywords='vpn tunnel openvpn zabbix',

    # Package info
    packages=find_packages(),
    python_requires='>=3.5',

    zip_safe=True,

    classifiers=[
        'Development Status :: 3 - Alpha',
        'Intended Audience :: Developers',
        'Intended Audience :: Information Technology',
        'Intended Audience :: Science/Research',
        'Intended Audience :: System Administrators',
        'License :: OSI Approved :: MIT License',
        'Operating System :: POSIX',
        'Programming Language :: Python'
    ]

)
