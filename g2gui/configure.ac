#                                               -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

AC_PREREQ(2.57)
AC_INIT(FULL-PACKAGE-NAME, VERSION, BUG-REPORT-ADDRESS)

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
# AC_CHECK_FILE(Datei_mit_Pfad,[Aktion_falls_gefunden],[Aktion_falls_nicht_gefunden])

AC_CHECK_FILE(jars/boot.jar,,[AC_MSG_ERROR([boot.jar not found])])
AC_CHECK_FILE(jars/jface.jar,,[AC_MSG_ERROR([jface.jar not found ])])
AC_CHECK_FILE(jars/regexp.jar,,[AC_MSG_ERROR([regexp.jar not found ])])
AC_CHECK_FILE(jars/rss4j.jar,,[AC_MSG_ERROR([rss4j.jar not found])])
AC_CHECK_FILE(jars/runtime.jar,,[AC_MSG_ERROR([runtime.jar not found])])
AC_CHECK_FILE(jars/trove.jar,,[AC_MSG_ERROR([trove.jar not found])])
AC_CHECK_FILE(jars/xercesImpl.jar,,[AC_MSG_ERROR([xercesImpl.jar not found])])
AC_CHECK_FILE(jars/xmlParserAPIs.jar,, [AC_MSG_ERROR([xmlParserAPIs.jar not found])])

# Checks for header files.

# Checks for typedefs, structures, and compiler characteristics.

# Checks for library functions.

AC_CONFIG_FILES([Makefile])
AC_OUTPUT
