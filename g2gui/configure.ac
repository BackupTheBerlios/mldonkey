#-*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

$FULL-PACKAGE-NAME=g2gui
$VERSION=0.2.0CVS
$BUG-REPORT-ADDRESS=https://developer.berlios.de/bugs/?group_id=610

AC_PREREQ(2.56)

AC_INIT(FULL-PACKAGE-NAME, VERSION, BUG-REPORT-ADDRESS)
AC_CONFIG_AUX_DIR(build)
AC_CANONICAL_HOST
AC_ARG_ENABLE(batch,
        AC_HELP_STRING([--enable-batch],
		       [downloads nescessary files from Internet, if they are not found at the right place (default is disabled)]),
		        [ac_cv_enable_batch=yes
			AC_PATH_PROG([WGET],[wget])
			   if test -z $ac_cv_path_WGET; then
			   	AC_MSG_ERROR([**** Could not find wget needed for enable-batch! ****])
			   fi
			AC_PATH_PROG([TAR],[tar])
			  if test -z $ac_cv_path_TAR; then
			  	AC_MSG_ERROR([**** Could not find tar needed for enable-batch! ****])
			  fi
			AC_PATH_PROG([BZIP2],[bzip2])
			  if test -z $ac_cv_path_BZIP2; then
			  	AC_MSG_ERROR([**** Could not find bzip2 needed for enable-batch! ****])
			  fi
			],
			ac_cv_enable_batch=no)


AC_ARG_WITH(fox,
        AC_HELP_STRING([--with-fox],
                        [uses fox as widget-set instead of default (gtk)]),
                        widget_set=fox)

AC_ARG_WITH(motif,
	    AC_HELP_STRING([--with-motif],
			            [uses motif as widget-set instead of default (gtk)]),
			            widget_set=motif)


# Checks for programs.

#take gcj from commandline ./configure (...) gcj=/path/to/gcj (...)


if test ! -z $gcj; then
ac_cv_path_GCJ=$gcj
fi

AC_PATH_PROG([GCJ],[gcj])
if test -z $ac_cv_path_GCJ; then
   AC_MSG_ERROR([**** Could not find the compiler gcj needed for this software! ****])
fi
GCJ_VERSION=`$ac_cv_path_GCJ -dumpversion`

#check for right version-number
oldIFS="$IFS"
IFS="."
set version_dummy $GCJ_VERSION
IFS="$oldIFS"
GCJ_MAJOR=$2
GCJ_MINOR=$3
GCJ_MICRO=$4

if test $GCJ_MAJOR -ge 3; then
	if  test $GCJ_MINOR -ge 3; then
	echo using gcj version $GCJ_MAJOR.$GCJ_MINOR.$GCJ_MICRO
	else
	AC_MSG_ERROR([**** wrong gcj version $GCJ_MAJOR.$GCJ_MINOR.$GCJ_MICRO, you need gcj >=3.3***])
	fi
else
AC_MSG_ERROR([**** wrong gcj version $GCJ_MAJOR.$GCJ_MINOR.$GCJ_MICRO, you need gcj >=3.3***])
fi

AC_PATH_PROG([FIND],[find])
if test -z $ac_cv_path_FIND; then
   AC_MSG_ERROR([**** Could not find the program find needed for this software! ****])
fi

AC_PATH_PROG([LD],[ld])
if test -z $ac_cv_path_LD; then
   AC_MSG_ERROR([**** Could not find the program 'ld' needed to compile this software! ****])
fi

AC_PATH_PROG([SED],[sed])
if test -z $ac_cv_path_SED; then
   AC_MSG_ERROR([**** Could not find the program 'sed' needed to compile this software! ****])
fi
   


# Checks for libraries.
# define URLS for libs
ac_lib_url="http://download.berlios.de/pub/mldonkey/g2gui-libs/swt3m5"
lib_dir=lib
lib_jar_dir=jar
src_dir=src
AC_SUBST(SRC_DIR,[$src_dir])

#define function, that checks for jars, and downloads them if nescerssary:
define([NEEDED_FILE_PRESENT],[
	AC_CHECK_FILE($lib_jar_dir/$1,,	[
		if test $ac_cv_enable_batch = yes; then
			echo "downloading $1.tar.bz2"
			$ac_cv_path_WGET -c $ac_lib_url/$1.tar.bz2 -P$lib_jar_dir
            $ac_cv_path_TAR -xjf $lib_jar_dir/$1.tar.bz2 -C $lib_jar_dir
            echo "extracted $1.tar.bz2"
			rm $lib_jar_dir/$1.tar.bz2
		else
		AC_MSG_ERROR([$1 not found, use --enable-batch to auto-download])
		fi
	])
])


NEEDED_FILE_PRESENT([gnu-regexp-1.1.4.jar])
NEEDED_FILE_PRESENT([gnu-trove-1.0.2.jar])
NEEDED_FILE_PRESENT([boot-2.1.jar])
NEEDED_FILE_PRESENT([jface-2.1.jar])
NEEDED_FILE_PRESENT([xercesImpl-2.1.jar])
NEEDED_FILE_PRESENT([xmlParserAPIs-2.1.jar])
NEEDED_FILE_PRESENT([Makefile.in])

#check for runtime-build dir
runtime_dir=runtime-2.1
AC_SUBST(RUNTIME_DIR,[$runtime_dir])
AC_CHECK_FILE([$lib_jar_dir/$runtime_dir/Makefile.in],, [
	if test $ac_cv_enable_batch = yes; then
	        echo "downloading $runtime_dir"
		$ac_cv_path_WGET $ac_lib_url/$runtime_dir.tar.bz2 -P$lib_jar_dir
		$ac_cv_path_TAR -xjf $lib_jar_dir/$runtime_dir.tar.bz2 -C $lib_jar_dir
        echo "extracted $runtime_dir.tar.bz2"
		rm $lib_jar_dir/$runtime_dir.tar.bz2
	else
	AC_MSG_ERROR([needed files not found, use --enable-batch to auto-download])
	fi
])


#now follows the platform-dependent stuff:
resources_dir=resources
AC_SUBST(RES_DIR,[$resources_dir])

case $host in
	*mingw32* |*win*)
	host=win
	swt_dir=swt-win32
	obj_dir=obj-$host
	platform_targets=icon.o
	AC_SUBST(MAGIC,["#no magic"])
	;;

	*)
	if !(test $widget_set); then
		widget_set=gtk
	fi
	# host=other
    host=$(echo $(uname) | tr A-Z a-z)
	swt_dir=swt-$host-$widget_set
	obj_dir=obj-$widget_set
	AC_SUBST(MAGIC,[$(magic)])
	echo "SWT widget-set : $widget_set"
	echo "SWT dir prefix : $swt_dir"
	;;
esac

AC_SUBST(PLATFORM_TARGETS,[$platform_targets])
AC_SUBST(SWT_DIR,[$swt_dir-ws])

# now we fetch the platform-specific jars + .so files

AC_CHECK_FILE([$lib_jar_dir/$swt_dir-ws/Makefile.in],, [
	if test $ac_cv_enable_batch = yes; then
		echo "downloading $swt_dir-ws"
		$ac_cv_path_WGET $ac_lib_url/$swt_dir-ws.tar.bz2 -P$lib_jar_dir
		$ac_cv_path_WGET $ac_lib_url/$swt_dir-os.tar.bz2 -P$lib_dir
		## need to to some platform specific stuff (extract win32 libs into basedir?)
        echo "downloading $swt_dir-os"
		$ac_cv_path_TAR -xjf $lib_jar_dir/$swt_dir-ws.tar.bz2 -C $lib_jar_dir
		$ac_cv_path_TAR -xjf $lib_dir/$swt_dir-os.tar.bz2 -C $lib_dir
		rm $lib_jar_dir/$swt_dir-ws.tar.bz2
		rm $lib_dir/$swt_dir-os.tar.bz2
	else
	AC_MSG_ERROR([needed files not found, use --enable-batch to auto-download])
	fi
])


# libXm.so.2.1  libswt-gnome-motif-2135.so  libswt-kde-motif-2135.so  libswt-motif-2135.so  swt.jar

# check for bogus file and CVS directories
if test -e src/net/mldonkey/g2gui/model/enum/EnumMaskMatcherTest.java; then
    echo "Deleting bogus file src/net/mldonkey/g2gui/model/enum/EnumMaskMatcherTest.java"
	rm src/net/mldonkey/g2gui/model/enum/EnumMaskMatcherTest.java
fi
# now creating Makefile-vars

#creating obj-dir
AC_SUBST(OBJ_DIR,[$obj_dir])
AC_SUBST(PACKAGE_DIR,[$PWD])

# Checks for header files.

# Checks for typedefs, structures, and compiler characteristics.

# Checks for library functions.

# prepare other Makefiles

AC_CONFIG_FILES([$lib_jar_dir/Makefile $lib_jar_dir/$swt_dir-ws/Makefile $lib_jar_dir/$runtime_dir/Makefile Makefile])
AC_OUTPUT

