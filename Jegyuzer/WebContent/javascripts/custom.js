$.ajaxSetup ({
    // Disable caching of AJAX responses
    cache: false
});
$(document).ready(function(){
	//$("#result-table").hide();
	
	$("#Results").click(function(){
		openTab("Results");
		$.ajax({
			url:"./ju/result",
			async:false,
			success:function(data){
				$("#result-table").dynatable({
					  dataset: {
					    records: data
					  }}
				  );
				  $("#result-table").show();
				},
		});
				
	});
});

function openTab(page) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(page).style.display = "block";
    document.getElementById(page).className += " active";
    
}