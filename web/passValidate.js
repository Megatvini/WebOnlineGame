
/**
 * Created by rezo on 6/30/15.
 */

function savePassword(){
    var panel = $("#passwordChange");
    var oldPass = $("#oldPassword")[0];
    var newPass = $("#newPassword")[0];
    var passAgain = $("#passAgain")[0];
    if(newPass.value==passAgain.value){


        $.ajax({
            url: "/passwordChange",
            method: "post",
            data: {
                oldPass: oldPass.value,
                newPass: newPass.value
            },
            success: function (data) {

                if(data=='2'){
                    data="პაროლი უნდა შედგებოდეს ლათინური ასოების, ციფრების, სასვენი ნიშნებისგან და უნდა შეიცავდეს მინიმუმ 5 სიმბოლოს";
                }else if(data=="1")
                    data="პაროლი შეიცვალა წარმატებით";
                else
                    data="პაროლი არასწორია";


                document.getElementById("validationInfo").innerText=data
            },
            error: function(data) {
                console.log(data)
            }

        });


    }else{
        document.getElementById("validationInfo").innerText="პაროლები არ ემთხვევა";
    }




    function validate() {

    }
}
