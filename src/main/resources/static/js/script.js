console.log("This is Script File")

function togglePasswordVisibility() {
      var x = document.getElementById("password_field");
      var show_eye = document.getElementById("show_eye");
      var hide_eye = document.getElementById("hide_eye");

      if (x.type === "password") {
        x.type = "text";
        show_eye.classList.add("d-none");
        hide_eye.classList.remove("d-block");
      } else {
        x.type = "password";
        show_eye.classList.remove("d-none");
        hide_eye.classList.add("d-block");
      }
    }
    
const toggleSidebar=()=>
{
	if($(".sidebar").is(":visible"))
	{ 	//true//close it 
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}else
	
	{	//false//show it 
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
}


function deleteContact(cid) {
	swal({
		title: "Are you sure you want to delete this contact..?",
		text: "Once deleted, you will not be able to recover this contact!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
		popup: "rounded-corner-alert",
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location="/user/delete/" +cid;
			} else {
				swal("Your Contact is safe..!!");
				
			}
		});
}