package com.bridgelabz.user.service;

import com.bridgelabz.user.model.User;
import com.bridgelabz.user.model.UserDto;

/**<p>This is UserService Interface</p>
 * @author bridgeit
 *
 */

public interface UserService {
    
	/**<p>This is registerService is going register new User in TODO Application
	 * @param userDto
	 * @param requestURL
	 */
	public void register(UserDto userDto, String requestURL);

	/**<p>This is loginService is used for login purpose</P>
	 * @param userDto
	 * @return
	 */
	public String login(UserDto userDto);

	/**<p>This is getUserByEmail Service it will return User on this requested email</p>
	 * @param user
	 * @return
	 */
	public User getUserByEmail(User user);

	/**<p>This is resetPassword service it will reset new password on particular email </p>
	 * @param userDto
	 * @return
	 */
	boolean resetPassword(UserDto userDto);

	/**<p>This is getUserEmailId service it will return respective EmailId of randomUUID </p>
	 * @param randomUUID
	 * @return
	 */
	public String getUserEmailId(String randomUUID);

	/**<p>This userActivation service is going to activate user on the basis of respected randomUUID </p>
	 * @param randomUUID
	 * @return
	 */
	boolean userActivation(String randomUUID);

	/**<p>This is forgetPassword service it will accept forgotPassword request 
	 * and it will redirect to user in resetPassword service</p>
	 * 
	 * @param email
	 * @param url
	 * @return
	 */
	public boolean forgetPassword(String email, String url);

	/**<p>This getIdByEmail service it will return userId on the basis of Email </p>
	 * @param email
	 * @return
	 */
	public int getIdByEmail(String email);

	/**<p> This is getUserById service it will return User of requested userId</p>
	 * @param userId
	 * @return
	 */
	public User getUserById(int userId);

}
