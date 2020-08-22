const frontRoot = "http://localhost:3000";
(function ($) {
	"use strict";

	// Options for Message
	//----------------------------------------------
	var options = {
		'btn-loading': '<i class="fa fa-spinner fa-pulse"></i>',
		'btn-success': '<i class="fa fa-check"></i>',
		'btn-error': '<i class="fa fa-remove"></i>',
		'msg-success': 'All Good! Redirecting...',
		'msg-error': 'Wrong login credentials!',
		'useAJAX': true,
	};

	// Login Form
	//----------------------------------------------
	// Validation
	$("#login-form").validate({
		rules: {
			lg_username: "required",
			lg_password: "required",
		},
		errorClass: "form-invalid"
	});

	// Form Submission
	$("#login-form").submit(function () {
		form_loading($(this));
		var thisForm = $(this);
		if (options['useAJAX'] == true) {
			// Dummy AJAX request (Replace this with your AJAX code)
			
			// formdata to JSON
			var formArray = $("#login-form").serializeArray();
			var data = {};
			$.each(formArray, function (i, v) {
				// data[`\"${v.name}\"`] = v.value;
				data[v.name] = v.value;
			})
			// 씨바 이렇게 stringify해서 "key":"value"로 넘겨줘야합니다 아니면 못알아먹네요씨바
			var string = JSON.stringify(data);
			console.log(string);
			// fetch("http://localhost:8090/login");
			$.ajax({
				url: "http://localhost:8090/login",
				method: "POST",
				contentType: 'application/json',
				dataType: "json",
				data: string,
				success: function (res) {
					console.log(res);
					// window.location.href = frontRoot;
				},
				error: function (a,b,c) {
					console.log("실패");
					// form_success(thisForm);
					remove_loading(thisForm);
					form_failed(thisForm);
				}
			})




			// If you don't want to use AJAX, remove this
			// dummy_submit_form($(this));

			// Cancel the normal submission.
			// If you don't want to use AJAX, remove this
			return false;
		}
	});



	// Loading
	//----------------------------------------------
	function remove_loading($form) {
		$form.find('[type=submit]').removeClass('error success');
		$form.find('.login-form-main-message').removeClass('show error success').html('');
	}

	function form_loading($form) {
		$form.find('[type=submit]').addClass('clicked').html(options['btn-loading']);
	}

	function form_success($form) {
		$form.find('[type=submit]').addClass('success').html(options['btn-success']);
		$form.find('.login-form-main-message').addClass('show success').html(options['msg-success']);
	}

	function form_failed($form) {
		$form.find('[type=submit]').addClass('error').html(options['btn-error']);
		$form.find('.login-form-main-message').addClass('show error').html(options['msg-error']);
	}

	// Dummy Submit Form (Remove this)
	//----------------------------------------------
	// This is just a dummy form submission. You should use your AJAX function or remove this function if you are not using AJAX.
	function dummy_submit_form($form) {
		if ($form.valid()) {
			form_loading($form);

			setTimeout(function () {
				form_success($form);
			}, 2000);
		}
	}

})(jQuery);
