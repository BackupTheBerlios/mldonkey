#-*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

$FULL-PACKAGE-NAME=g2gui
$VERSION=0.1.1
$BUG-REPORT-ADDRESS=https://developer.berlios.de/bugs/?group_id=610

AC_PREREQ(2.56)

AC_INIT(FULL-PACKAGE-NAME, VERSION, BUG-REPORT-ADDRESS)
AC_CONFIG_AUX_DIR(./build-stuff)
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

AC_ARG_WITH(motif,
	AC_HELP_STRING([--with-motif],
			[uses motif as widget-set instead of default (gtk)]),
			widget_set=motif,
			widget_set=gtk)


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
ac_lib_url="http://download.berlios.de/pub/mldonkey/g2gui-libs/swt3"
jar_dir=jar-libs

#define function, that checks for jars, and downloads them if nescerssary:

src_dir=src
AC_SUBST(SRC_DIR,[$src_dir])

define([NEEDED_FILE_PRESENT],[
	AC_CHECK_FILE($jar_dir/$1,,	[
		if test $ac_cv_enable_batch = yes; then
			echo downloading $1
			$ac_cv_path_WGET $ac_lib_url/$1 -P$jar_dir
		else 
		AC_MSG_ERROR([$1 not found, use --enable-batch to auto-download])
		fi
	])
])


NEEDED_FILE_PRESENT([boot.JDK.jar])
NEEDED_FILE_PRESENT([gnu-regexp-1.1.4.jar])
NEEDED_FILE_PRESENT([jface.jar])
NEEDED_FILE_PRESENT([Makefile.in])
NEEDED_FILE_PRESENT([rss4j.jar])
NEEDED_FILE_PRESENT([trove.jar])
NEEDED_FILE_PRESENT([xercesImpl.jar])
NEEDED_FILE_PRESENT([xmlParserAPIs.jar])


#check for runtime-build dir
AC_CHECK_FILE([$jar_dir/runtime/Makefile.in],, [
	if test $ac_cv_enable_batch = yes; then
	        echo downloading runtime
		$ac_cv_path_WGET $ac_lib_url/runtime.tar.bz2
		$ac_cv_path_TAR -xjf runtime.tar.bz2
		rm runtime.tar.bz2		
	else
	AC_MSG_ERROR([needed files not found, use --enable-batch to auto-download])
	fi
])

#now follow the platform-dependent stuff:

resources_dir=resources

AC_SUBST(RES_DIR,[$resources_dir])

case $host in
	*mingw32* |*win*)
	host=win	
	swt_dir=swt-win
	obj_dir=obj-win
	platform_targets=icon.o
	;;
	*)
	if !(test $widget_set); then
		widget_set=gtk
	fi
	host=other
	echo using widget-set : $widget_set
	swt_dir=swt-$widget_set
	obj_dir=obj-$widget_set
	;;
esac

AC_SUBST(PLATFORM_TARGETS,[$platform_targets])
AC_SUBST(SWT_DIR,[$swt_dir])

AC_CHECK_FILE([$jar_dir/$swt_dir/Makefile.in],, [
	if test $ac_cv_enable_batch = yes; then
		echo downloading $swt_dir
		$ac_cv_path_WGET $ac_lib_url/$swt_dir.tar.bz2
		$ac_cv_path_TAR -xjf $swt_dir.tar.bz2
		rm $swt_dir.tar.bz2
	else
	AC_MSG_ERROR([needed files not found, use --enable-batch to auto-download])
	fi
])


# libXm.so.2.1  libswt-gnome-motif-2135.so  libswt-kde-motif-2135.so  libswt-motif-2135.so  swt.jar


# now creating Makefile-vars

#creating obj-dir
AC_SUBST(OBJ_DIR,[$obj_dir])
AC_SUBST(PACKAGE_DIR,[$PWD])

# Checks for header files.

# Checks for typedefs, structures, and compiler characteristics.

# Checks for library functions.

AC_CONFIG_FILES([jar-libs/Makefile jar-libs/runtime/Makefile jar-libs/$swt_dir/Makefile Makefile])
AC_OUTPUT


