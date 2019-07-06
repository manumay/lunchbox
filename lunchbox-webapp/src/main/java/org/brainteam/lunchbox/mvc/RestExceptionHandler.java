package org.brainteam.lunchbox.mvc;import org.brainteam.lunchbox.core.AppContext;import org.brainteam.lunchbox.json.JsonException;import org.brainteam.lunchbox.security.Security;import org.springframework.http.HttpStatus;import org.springframework.security.access.AccessDeniedException;import org.springframework.web.bind.annotation.ControllerAdvice;import org.springframework.web.bind.annotation.ExceptionHandler;import org.springframework.web.bind.annotation.ResponseBody;@ControllerAdvicepublic class RestExceptionHandler {		private static final long STATUS_UNDEFINED = 0L;	@ExceptionHandler(RuntimeException.class)	public @ResponseBody JsonException handleRuntimeException(RuntimeException e) {		return toJsonException(e);	}		public static JsonException toJsonException(Throwable t) {		JsonException json = new JsonException();		json.setErrorCode(getErrorCode(t));		json.setError(t.getClass().getName());		json.setMessage(t.getLocalizedMessage());		return json;	}		protected static long getErrorCode(Throwable t) {		if (t instanceof AccessDeniedException && !AppContext.getBean(Security.class).isLoggedIn()) {			return HttpStatus.UNAUTHORIZED.value();		}		return STATUS_UNDEFINED;	}	}