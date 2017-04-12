/**
 * Created by SK on 28.03.2017.
 */
    

    function decreaseDeep() {
        deep--;
        zIndex--;
        modalStrings.pop();
        console.log(objects);
        objects.pop();
        console.log(objects);
        Data = objects[deep];
        var exStr = '';
        for(var str in modalStrings)
            exStr+=modalStrings[str];

        $('#modalWindow').html(exStr);
        document.body.removeChild(document.getElementsByClassName('modal-backdrop fade in')[document.getElementsByClassName('modal-backdrop fade in').length-1]);
    }

    function RecursionModals(data) {
        Data = data;
        var modalString = '<div id="modalWindow'+deep+'" class="modal fade in" style="z-index: '+zIndex+';display: block"><div class="modal-dialog"><div class="modal-content"><div class="modal-header"  id="headID"><button class="close" onclick="decreaseDeep()" type="button" id="closeBtn" data-dismiss="modal">Close</button></div><div class="modal-body">custom</div><div  class="modal-footer" onclick="decreaseDeep()"><button class="btn btn-default"  id="closeBtn" type="button" data-dismiss="modal">Close</button></div></div></div></div>';
        childModal = '\'#modalWindow'+deep+'\'';
        deep++;
        parentModal = childModal;
        zIndex++;
        var headerString='';
        var table = '<table class="table table-bordered table-hover">HB</table>';
        var headers= '<thead><tr>header</tr></thead>';
        var bodyM = '<tbody><tr>bodys</tr></tbody>';
        var patternModal = /custom/;
        var patternHead = /header/;
        var patternBody = /bodys/;
        var patternTable = /HB/;

        for(var key in data) {
            headerString+='<th>'+key+'</th>';
        }
        var additionalString = '';

        for(var key in data) {

            if($.isPlainObject(data[key]))
            {
                additionalString +='<td><input type="button" style="width: 100%" value="'+data[key]+'" data-toggle="modal" data-target="#modalWindow'+deep+'" onclick="GenerateModals(this)"></td>';
            }else
                additionalString +='<td>'+data[key]+'</td>';
        }

        headers = headers.replace(patternHead,headerString);
        bodyM = bodyM.replace(patternBody,additionalString);
        table = table.replace(patternTable,headers + bodyM);
        modalString = modalString.replace(patternModal,table);
        modalStrings.push(modalString);
        var exStr = '';
        for(var str in modalStrings)
            exStr+=modalStrings[str];

        $('#modalWindow').html(exStr);

    }

    function GenerateModals(obj) {
        temporaryData = Data;
        if(deep==0 && objects.length==0)
            objects.push(temporaryData);
        var row = obj.closest("tr").rowIndex;
        var col = obj.closest("td").cellIndex;

        if(temporaryData instanceof window.Array)
            objects.push(temporaryData[row-1][Object.keys(temporaryData[row-1])[col]]);
        else
            objects.push(temporaryData[Object.keys(temporaryData)[col]]);

        console.log(objects);
        RecursionModals(objects[deep+1]);
    }

    function UpdateData() {
        console.log("1");
    }

    var NameTable = "";
    var Data;
    var temporaryData;
    var zIndex = 1050;
    var modalStrings = new Array();
    var objects = new Array();
    var deep = 0;
    var parentModal = '';
    var childModal = '#modalWindow0';

    function DeleteRow(obj) {
        document.getElementById('tableHotel').deleteRow(obj.closest("tr").rowIndex);
        $.ajax({
            type: 'DELETE',
            url: '/servlet?tableName='+NameTable +'&action=REMOVE',
            data:{'id':obj.closest("tr").firstChild.textContent}
        });
    }

    function setHtml(){
        var countRows = Data.length;

        var headerString = '';
        var bodyString = '';
        var j = 0;
        var countColumn = 0;
        zIndex = 1050;
        for(var key in Data[0]) {
            headerString+='<th>'+key+'</th>';
            countColumn++;
        }
        while(j!=countRows){
            var strRow = '<tr class="id'+Data[j].id+'" style="border: none">row</tr>';
            var patternRow = /row/;
            var additionalString = '';
            for(var key in Data[j]) {

                if($.isPlainObject(Data[j][key]))
                {
                    additionalString +='<td><input type="button" style="width: 100%" value="'+Data[j][key]+'" data-toggle="modal" data-target="#modalWindow'+deep+'" onclick="GenerateModals(this)"></td>';
                }else
                    additionalString +='<td>'+Data[j][key]+'</td>';
            }
            additionalString+='<td style="border: none"><input type="button" style="width: 100%" value="UPDATE" onclick="UpdateData((this.parentNode).parentNode)"></td>' +
                '<td style="border: none"><input type="button" style="width: 100%" value="DELETE" onclick="DeleteRow(this)"></td>';
            bodyString += strRow.replace(patternRow,additionalString);
            j++;
        }


        var headers= '<thead><tr>header</tr></thead>';
        var body = '<tbody>body</tbody>';
        var patternHead = /header/;
        var patternBody = /body/;
        headers = headers.replace(patternHead,headerString);
        body = body.replace(patternBody,bodyString);
        $('#tableHotel').html(headers + body);
        
    }
$(document).ready(function() {

    
    $('.col-lg-3').on('click', function(event) {
        var target = event.target;

        if(!target.closest('td')) return;

        var nameTable = target.closest('td').childNodes[0].value;
        NameTable = nameTable;
        $.ajax({
            type: 'GET',
            url: '/servlet?tableName='+nameTable +'&action=GET_ALL',
            success: function(data) {
                console.log(data);
                objects = new Array();
                Data = data;
                setHtml();
            }
        });
    });
});