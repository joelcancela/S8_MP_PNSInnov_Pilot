function deleteConfirm(fileid, filename) {
    var modalID = fileid.replace(/\./g, "").replace(/\s/g, '');
    var dom = "<!-- Modal -->\n" +
        "  <div class=\"modal fade\" id=\"modalDeletion" + modalID + "\" role=\"dialog\">\n" +
        "    <div class=\"modal-dialog\">\n" +
        "    \n" +
        "      <!-- Modal content-->\n" +
        "      <div class=\"modal-content\">\n" +
        "        <div class=\"modal-header\">\n" +
        "          <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n" +
        "          <h4 class=\"modal-title\">Warning - File deletion</h4>\n" +
        "        </div>\n" +
        "        <div class=\"modal-body\">\n" +
        "          <p>Are you sure you want to delete the file '<b>" + filename + "</b>' ?</p>\n" +
        "<button type=\"button\" onclick=\"deleteFilePost('"+fileid+"')\" class=\"btn btn-danger\"" +
        " data-dismiss=\"modal\">Yes," +
        " delete" +
        " the" +
        " file</button>" +
        "        </div>\n" +
        "        <div class=\"modal-footer\">\n" +
        "          <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">No, abort !</button>\n" +
        "        </div>\n" +
        "      </div>\n" +
        "      \n" +
        "    </div>\n" +
        "  </div>";
    if($('#modalDeletion'+modalID).length == 0){
        $('body').append(dom);
    }
    $('#modalDeletion'+modalID).modal();
}

function deleteFilePost(fileid){
    $.ajax({
        type: 'POST',
        url: 'deleteDrive',
        data: {
            'fileid': fileid
        },
        success: function(msg){
            console.log("File deleted");
        }
    });
}

