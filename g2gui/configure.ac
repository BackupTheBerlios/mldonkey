#                                               -*- Autoconf -*-
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
ac_lib_url="http://download.berlios.de/pub/mldonkey/g2gui-libs"

#define function, that checks for jars, and downloads them if nescerssary:

src_dir=src
jar_dir=jars
AC_SUBST(SRC_DIR,[$src_dir])

define([NEEDED_JAR_PRESENT],[
	AC_CHECK_FILE(jars/$1,,	[
		if test $ac_cv_enable_batch = yes; then
			echo downloading $1
			$ac_cv_path_WGET $ac_lib_url/$1 -P$jar_dir
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

#now follow the platform-dependent stuff:
jar_dir=$PWD/jars
AC_SUBST(JAR_DIR,[$jar_dir])


resources_dir=resources

AC_SUBST(RES_DIR,[$resources_dir])

case $host in
	*mingw32* |*win*)
	host=win	
	swt_dir=swt-win
	swt_server_dir=swt-win
	obj_dir=$PWD/obj-win
	AC_PATH_PROG([JAR],[jar])
	if test -z $ac_cv_path_JAR; then
		AC_MSG_ERROR([**** Could not find the program 'jar' needed for compilation on win32! ****])
	fi 
	AC_SUBST(PLATFORM_TARGET,[icon.o])
	;;
	*)
	if !(test $widget_set); then
		widget_set=gtk
	fi
	host=other
	echo using widget-set : $widget_set
	swt_server_dir=swt-$widget_set
	swt_dir=$PWD/swt-$widget_set
	obj_dir=$PWD/obj-$widget_set
	;;
esac

AC_SUBST(SWT_DIR,[$swt_dir])

define([SWT_FILE_PRESENT],[
	AC_CHECK_FILE($swt_dir/$1,, [
		if test $ac_cv_enable_batch = yes; then
			echo downloading $1
				$ac_cv_path_WGET $ac_lib_url/$swt_server_dir/$1 -P$swt_dir		
		else
		AC_MSG_ERROR([$1 not found, use --enable-batch to auto-download])
		fi
	])
])

if test $host = win; then
	SWT_FILE_PRESENT([swt.jar])
	SWT_FILE_PRESENT([swt-win32-2135.dll])
else 
	if test $widget_set = gtk; then
		SWT_FILE_PRESENT([swt.jar])
		SWT_FILE_PRESENT([swt-pi.jar])
		SWT_FILE_PRESENT([libswt-gnome-gtk-2135.so])
		SWT_FILE_PRESENT([libswt-gtk-2135.so])
		SWT_FILE_PRESENT([libswt-pi-gtk-2135.so])
	else
		SWT_FILE_PRESENT([libXm.so.2.1])
		SWT_FILE_PRESENT([libswt-gnome-motif-2135.so])
		SWT_FILE_PRESENT([libswt-kde-motif-2135.so])
		SWT_FILE_PRESENT([libswt-motif-2135.so])
		SWT_FILE_PRESENT([swt.jar])
	fi
fi

# libXm.so.2.1  libswt-gnome-motif-2135.so  libswt-kde-motif-2135.so  libswt-motif-2135.so  swt.jar


# now creating Makefile-vars

AC_SUBST(CLASSPATH,[`$ac_cv_path_FIND $jar_dir $swt_dir -name "*.jar" -printf "%p:"`$src_dir:. ])

#creating obj-dir
mkdir -p $obj_dir
AC_SUBST(OBJ_DIR,[$obj_dir])
AC_SUBST(PACKAGE_DIR,[$PWD])

# Checks for header files.

# Checks for typedefs, structures, and compiler characteristics.

# Checks for library functions.

AC_CONFIG_FILES([Makefile])
AC_OUTPUT
