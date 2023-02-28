package com.github.cylyl.springdrop.account;

/**
 * user
 * group (role)
 * permission - owner permission | group permission | other permission
 * resource - A owned by B , A permissions 777
 * permission table AB (aid, bid, 777)
 *
 * Number	Permission Type		    Symbol
 * 0	    No Permission		    —
 * 1	    Execute			        –x
 * 2	    Write			        -w-
 * 3	    Execute + Write		    -wx
 * 4	    Read			        r–
 * 5	    Read + Execute		    r-x
 * 6	    Read +Write		        rw-
 * 7	    Read + Write +Execute	rwx
 */
public class Concept {
}
