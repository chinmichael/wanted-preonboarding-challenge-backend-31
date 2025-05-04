package com.wanted.cqrs.common.response;

import com.wanted.cqrs.common.utils.Mappers;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class ResponseUtils {

    /*

     */

    public static void printSuccessResponse(HttpServletResponse response) throws IOException {
        ResponseUtils.printSuccessResponse(response, null, null);
    }

    public static <T> void printSuccessResponse(HttpServletResponse response, T data) throws IOException {
        ResponseUtils.printSuccessResponse(response, data, null);
    }

    public static <T, E> void printSuccessResponse(HttpServletResponse response, T data, String message) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponseDto<T, E> printBody =
                (data != null && message != null)
                        ? ResponseUtils.ok(data, message) : data != null
                        ? ResponseUtils.ok(data) : ResponseUtils.ok();

        response.getWriter().print(Mappers.JSON_WRITER.writeValueAsString(printBody));
    }

    public static void printErrorResponse(HttpServletResponse response, ApiErrorEnum error) throws IOException {

    }

    public static <T, E> void printErrorResponse(HttpServletResponse response, ApiErrorEnum error, E details) throws IOException {
        response.setStatus(error.getHttpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponseDto<T, E> printErrorBody = details != null
                ? ResponseUtils.error(error, details) : ResponseUtils.error(error);

        response.getWriter().print(Mappers.JSON_WRITER.writeValueAsString(printErrorBody));
    }

    /*

     */

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> success() {
        return ResponseEntity.ok(ResponseUtils.ok());
    }

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> success(T data) {
        return ResponseEntity.ok(ResponseUtils.ok(data));
    }

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> success(T data, String message) {
        return ResponseEntity.ok(ResponseUtils.ok(data, message));
    }

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> create() {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.ok());
    }

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> create(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.ok(data));
    }

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> create(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.ok(data, message));
    }

    /*

     */

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> fail(ApiErrorEnum error) {
        return ResponseEntity.status(error.getHttpStatus()).body(ResponseUtils.error(error));
    }

    public static <T, E>ResponseEntity<ApiResponseDto<T, E>> fail(ApiErrorEnum error, E details) {
        return ResponseEntity.status(error.getHttpStatus()).body(ResponseUtils.error(error, details));
    }

    public static <T, E> ApiResponseDto<T, E> ok() {
        return ApiResponseDto.<T, E>builder()
                .success(true)
                .message("요청이 성공적으로 처리되었습니다.")
                .build();
    }

    public static <T, E> ApiResponseDto<T, E> ok(@Nullable T data) {
        return ApiResponseDto.<T, E>builder()
                .success(true)
                .data(data)
                .message("요청이 성공적으로 처리되었습니다.")
                .build();
    }

    public static <T, E> ApiResponseDto<T, E> ok(@Nullable T data, String message) {
        return ApiResponseDto.<T, E>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T, E> ApiResponseDto<T, E> error(ApiErrorEnum error) {
        return ApiResponseDto.<T, E>builder()
                .success(false)
                .error(ApiError.<E>builder().error(error).build())
                .build();
    }

    public static <T, E> ApiResponseDto<T, E> error(ApiErrorEnum error, @Nullable E details) {
        return ApiResponseDto.<T, E>builder()
                .success(false)
                .error(ApiError.<E>builder().error(error).details(details).build())
                .build();
    }
}
