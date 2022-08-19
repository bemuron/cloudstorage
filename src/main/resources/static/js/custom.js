
var mNoteId;

//create a new note
function createNote(){
    var noteTitle = document.getElementById("note-title").value;
    var noteDesc = document.getElementById("note-description").value;

    if(noteTitle.trim() === "" || noteDesc.trim() === ""){
        alert("Please fill in the necessary fields");
    }else{
        document.getElementById('note_form').submit();
        console.log("note title "+ noteTitle);
        /*$.ajax({
            url: "/create-note",
            type: 'post',
            beforeSend: function () { // show loading spinner
                $('#loader').removeClass('hidden');
            },
            //dataType: 'json',
            data: {
                note_name: noteTitle.trim(),
                description: noteDesc.trim()
            },
            success: function(data) {
                //console.log(data);
                if(data != null || data > 0){
                    document.getElementById("dep_asset_cost").value = assetCost;
                    alert("Asset created \n "+data);
                    mNoteId = data;
                    $("#noteModalLabel").modal("hide");

                }else{
                  alert("Note not created \n "+data);

                }
            },
            complete: function () { // hiding the spinner.
                $('#loader').addClass('hidden');
            }
        });*/
    }
}