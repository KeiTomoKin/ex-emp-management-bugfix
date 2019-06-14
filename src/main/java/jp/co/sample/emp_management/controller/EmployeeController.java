package jp.co.sample.emp_management.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.InsertEmployeeForm;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	@ModelAttribute
	public InsertEmployeeForm setUpInsertForm() {
		return new InsertEmployeeForm();
	}
	
	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/showList")
	public String showList(Model model) {
		List<Employee> employeeList = employeeService.showList();
		List<String> nameList = new ArrayList<String>();
		for(Employee employee:employeeList) {
			nameList.add("\""+employee.getName()+"\"");
		}
		model.addAttribute("employeeList", employeeList);
		model.addAttribute("nameList", nameList);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	@RequestMapping("/searchByName")
	public String searchByName(Model model, String name) {
		List<Employee> employeeList = employeeService.findByAmbiguousName(name);
		if (employeeList.size() == 0) {
			model.addAttribute("noResult", true);
			model.addAttribute("name", name);
			return showList(model);
		}
		model.addAttribute("employeeList", employeeList);
		employeeList = employeeService.showList();
		List<String> nameList = new ArrayList<String>();
		for(Employee employee:employeeList) {
			nameList.add("\""+employee.getName()+"\"");
		}
		model.addAttribute("nameList", nameList);
		return "employee/list";
	}

	@RequestMapping("/register")
	public String register() {
		return "employee/insertEmployee";
	}
	
	@RequestMapping("/insert")
	public String insert(InsertEmployeeForm insertEmployeeForm, Model model)
			throws IllegalStateException, IOException, ParseException {
		System.out.println(insertEmployeeForm);
		String uploadfile ="src/main/resources/static/img/" + insertEmployeeForm.getImage().getOriginalFilename();
		try (FileOutputStream os = new FileOutputStream(uploadfile)) {
			byte[] bytes = insertEmployeeForm.getImage().getBytes();
			os.write(bytes);
		} catch (IOException ex) {
			System.err.println(ex);
		}
		Employee employee =new Employee();
		BeanUtils.copyProperties(insertEmployeeForm, employee);
		String inpDateStr = insertEmployeeForm.getYear()+"/"+insertEmployeeForm.getMonth()+"/"+insertEmployeeForm.getDay();

		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy/MM/dd");

		Date dateTime = sdformat.parse(inpDateStr);
		employee.setImage(insertEmployeeForm.getImage().getOriginalFilename());
		employee.setHireDate(dateTime);
		System.out.println(employee);
		employeeService.insert(employee);
		return "redirect:/employee/showList";
	}
}
