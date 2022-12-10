package com.lgl.mes.system.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author dreamer，75039960@qq.com
 * @date 20211/0/31 10:00
 */
public class SpUsernamePasswordToken extends UsernamePasswordToken {

	/**
	 * 登录账户类型
	 */
	private String type;

	public SpUsernamePasswordToken(final String username, final String password, String loginType) {
		super(username, password);
		this.type = loginType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
