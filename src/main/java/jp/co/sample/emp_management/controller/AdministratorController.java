package jp.co.sample.emp_management.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private HttpSession session;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertAdministratorForm setUpInsertAdministratorForm() {
		return new InsertAdministratorForm();
	}

	// (SpringSecurityに任せるためコメントアウトしました)
//	@ModelAttribute
//	public LoginForm setUpLoginForm() {
//		return new LoginForm();
//	}

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 * 
	 * @return 管理者登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		String token = UUID.randomUUID().toString();
		session.setAttribute("token", token);
		return "administrator/insert";
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン画面へリダイレクト
	 */
	@RequestMapping("/insert")
	public String insert(String token, @Validated InsertAdministratorForm form, BindingResult result, Model model) {
		if(administratorService.findByMailAddress(form.getMailAddress())!=null) {
			result.rejectValue("mailAddress", null, "登録済みのメールアドレスです");
		}
		if (!form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("confirmPassword", null, "パスワードと確認用パスワードが一致しません");
		}
		if (result.hasErrors()) {
			return toInsert();
		}
		String tokenInSession = (String) session.getAttribute("token");
		if (tokenInSession.equals(token)) {
			Administrator administrator = new Administrator();
			// フォームからドメインにプロパティ値をコピー
			BeanUtils.copyProperties(form, administrator);
			administratorService.insert(administrator);
		}
		session.removeAttribute("token");
		return "redirect:/";

	}

	/////////////////////////////////////////////////////
	// ユースケース：ログインをする
	/////////////////////////////////////////////////////
	/**
	 * ログイン画面を出力します.
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping("/")
	public String toLogin(Model model,@RequestParam(required = false) String error, HttpServletRequest request) {
		System.out.println("リファラ情報" + request.getHeader("referer"));
		System.err.println("login error:" + error);
		if (error != null) {
			System.err.println("login failed");
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
		}
		return "administrator/login";
	}

//	/**
//	 * ログインします.
//	 * 
//	 * @param form   管理者情報用フォーム
//	 * @param result エラー情報格納用オブッジェクト
//	 * @return ログイン後の従業員一覧画面
//	 */
//	@RequestMapping("/login")
//	public String login(LoginForm form, BindingResult result, Model model) {
//		Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
//		if (administrator == null) {
//			result.addError(new ObjectError("loginError", "メールアドレスまたはパスワードが不正です。"));
//			return toLogin();
//		}
//		session.setAttribute("administratorName", administrator.getName());
//		return "forward:/employee/showList";
//	}

	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
//	@RequestMapping(value = "/logout")
//	public String logout() {
//		session.invalidate();
//		return "redirect:/";
//	}

}
