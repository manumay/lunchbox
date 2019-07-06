function uploadComplete() {
	$('.fileupload').fileupload('reset')
	$('.uploadinfo').hide();
	$('.uploadbutton').attr('disabled', false);
	$('#alerts').append('<div class="alert alert-success">' +
			  '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
			  '<strong>Erfolg:</strong> Import abgeschlossen.' +
			  '</div>');
}

function uploadFailed() {
	$('#alerts').append('<div class="alert alert-error">' +
	  '<button type="button" class="close" data-dismiss="alert">&times;</button>' +
	  '<strong>Warnung!</strong> Fehler beim Import.' +
	  '</div>');
}