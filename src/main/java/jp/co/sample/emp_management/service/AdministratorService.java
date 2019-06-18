package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository administratorRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 管理者情報を登録します. 
	 * パスワードはここでハッシュ化されます
	 * 
	 * @param administrator 管理者情報
	 */
	public void insert(Administrator administrator) {
		administrator.setPassword(encodePassword(administrator.getPassword()));
		administratorRepository.insert(administrator);
	}

//	/**
//	 * ログインをします.
//	 * 
//	 * @param mailAddress メールアドレス
//	 * @param password    パスワード
//	 * @return 管理者情報 存在しない場合はnullが返ります
//	 */
//	public Administrator login(String mailAddress, String passward) {
//		Administrator administrator = administratorRepository.findByMailAddressAndPassward(mailAddress, passward);
//		return administrator;
//	}

	/**
	 * メールアドレスから管理者情報を取得します.
	 * 
	 * @param mailAddress メールアアドレス
	 * @return administrator 管理者情報
	 */
	public Administrator findByMailAddress(String mailAddress) {
		return administratorRepository.findByMailAddress(mailAddress);

	}

	/**
	 * パスワードをハッシュ化する.
	 * 
	 * @param rawPassword ハッシュ化前のパスワード(元のパスワード)
	 * @return ハッシュ化後のパスワード
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

}
