package jp.co.sample.emp_management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * アプリケーション内で処理されなかった例外をここでキャッチし、
 * エラーページへ遷移させます.
 * 
 * @author igamasayuki
 *
 */
@Component
public class GlobalExceptionHandler implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) {
		logger.error("システムエラーが発生しました！", e);
		e.printStackTrace();
		return new ModelAndView("redirect:/maintenance");
	}
}