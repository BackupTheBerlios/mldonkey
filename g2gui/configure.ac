#                                               -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

AC_PREREQ(2.57)
AC_INIT(FULL-PACKAGE-NAME, VERSION, BUG-REPORT-ADDRESS)
AC_ARG_ENABLE(batch,
        AC_HELP_STRING([--enable-batch],
		       [downloads nescessary files from Internet, if they are not found at the right place (default is disabled)]),
		        [ac_cv_enable_batch=yes
			AC_PATH_PROG([WGET],[wget])
			   if test -z $ac_cv_path_WGET; then
			   	AC_MSG_ERROR([**** Could not find wget needed for enable-batch! ****])
			   fi
			], 
			ac_cv_enable_batch=no)

# Checks for programs.

AC_PATH_PROG([GCJ],[gcj])
if test -z $ac_cv_path_GCJ; then
   AC_MSG_ERROR([**** Could not find the compiler gcj needed for this software! ****])
fi
GCJ_VERSION=`$ac_cv_path_GCJ -dumpversion`
#TODO: add a check, that looks if compiler is recent enough

AC_PATH_PROG([FIND],[find])
if test -z $ac_cv_path_FIND; then
   AC_MSG_ERROR([**** Could not find the program find needed for this software! ****])
fi 


# Checks for libraries.
# define URLS for libs
ac_lib_url="http://download.berlios.de/pub/mldonkey/g2gui-libs"

#define function, that checks for jars, and downloads them if nescerssary:

define([NEEDED_JAR_PRESENT],[
	AC_CHECK_FILE(jars/$1,,	[
		if test $ac_cv_enable_batch = yes; then
			echo downloading $1
			$ac_cv_path_WGET $ac_lib_url/$1 -Pjars
		else 
		AC_MSG_ERROR([$1 not found, use --enable-batch to auto-download])
		fi
	])
])

NEEDED_JAR_PRESENT([[boot.jar]])
NEEDED_JAR_PRESENT([[jface.jar]])
NEEDED_JAR_PRESENT([[gnu-regexp-1.1.4.jar]])
NEEDED_JAR_PRESENT([[rss4j.jar]])
NEEDED_JAR_PRESENT([[runtime.jar]])
NEEDED_JAR_PRESENT([[trove.jar]])
NEEDED_JAR_PRESENT([[xercesImpl.jar]])
NEEDED_JAR_PRESENT([[xmlParserAPIs.jar]])

#checks for swt:
ac_system_type=`uname -s`
define([SWT_FILE_PRESENT],[
	AC_CHECK_FILE(swt/$1,, [
		if test $ac_cv_enable_batch = yes; then
			echo downloading $1
			if test $ac_system_type = Linux; then
				$ac_cv_path_WGET $ac_lib_url/swt-gtk/$1 -Pswt
			else
				AC_MSG_ERROR([You are not using Linux, auto-downloading of SWT-files only implemented for \
								linux-gtk yet, contavt g2-gui-team for more information])
			fi
		else
		AC_MSG_ERROR([$1 not found, use --enable-batch to auto-download])
		fi
	])
])
if test $ac_system_type = Linux; then
	SWT_FILE_PRESENT([swt.jar])
	SWT_FILE_PRESENT([swt-pi.jar])
	SWT_FILE_PRESENT([libswt-gnome-gtk-2135.so])
	SWT_FILE_PRESENT([libswt-gtk-2135.so])
	SWT_FILE_PRESENT([libswt-pi-gtk-2135.so])
else
	AC_MSG_WARN([TODO: add chekcs for win32 and !=Linux])
fi



# Checks for header files.

# Checks for typedefs, structures, and compiler characteristics.

# Checks for library functions.

AC_CONFIG_FILES([Makefile])
AC_OUTPUT
