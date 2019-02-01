package com.jfinalshop.shiro.freemarker;

/**
 * <p>Equivalent to {@link org.apache.shiro.web.tags.HasPermissionTag}</p>
 *
 * @since 0.1
 */
public class HasPermissionTag extends PermissionTag {
  protected boolean showTagBody(String p) {
	  System.out.println("----------------"+p);
    return isPermitted(p);
  }
}
