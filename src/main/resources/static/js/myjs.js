/**
 * 模糊查找
 * 指定table中是否存在指定字符，存在则改变颜色，不存在则隐藏，如果字符为空，这显示所有
 * @param {} find 查找数据
 * @param {} tableId 要操作标的id
 * @param {} callBack 回调函数，当find为控制，指定执行方法，
 *           如果callBack==undefind||callBack==null时，显示所有行，
 */
function SearchTable(find, tableId, callBack) {
    this.find = find.replace(" ", "");
    this.flg = false;
    this.callBack = callBack;
    this.table = document.getElementById(tableId);
    this.tbody = this.table.getElementsByTagName("tbody")[0];
    this.tr = this.tbody.getElementsByTagName("tr");
    this.dataType = function (tr) {
        var d = tr.getAttribute("data-type");
        return d;
    }
    this.tdfor = function () {
        var flg = false;
        var td = this.td;
        for (var d = 0; d < td.length; d++) {
            if (td[d].innerText.toLowerCase().indexOf(this.find) >= 0 && this.find.length > 0) {
                var _html = "<span class='searchSpan'>" + td[d].innerText + "<span>";
                td[d].innerText = "";
                td[d].innerHTML = _html;
                flg = true;
            } else {
                var span = td[d].getElementsByClassName("searchSpan");
                if (span.length <= 0) {
                    continue;
                }
                for (var s = 0; s < span.length; s++) {
                    var te = span[s].innerText;
                    td[d].innerText += te;
                }
            }
        }
        return flg;
    }
    this.trfor= function() {
        for (var i = 0; i < this.tr.length; i++) {
            var t = this.tr[i];
            if (this.dataType(t)!=="data") {
                continue;
            }
            this.td = t.getElementsByTagName("td");
            this.flg=this.tdfor();
            var innerHTML = t.innerHTML;
            if (innerHTML.toLowerCase().indexOf(this.find) >= 0) {
                t.style.display = "";
            } else {
                t.style.display = "none";
            }
            t.style.display = this.flg ? "" : "none";
        }
    }
    this.trforShow= function() {
        for (var r = 0; r < this.tr.length; r++) {
            if (this.dataType(this.tr[r]) !== "data") {
                continue;
            }
            this.td = this.tr[r].getElementsByTagName("td");
            this.flg = this.tdfor();
            this.tr[r].style.display = "";
        }
    }
    this.callbacks = function () {
        if (this.callBack === undefined || this.callBack == null) {
            this.trforShow();
            return;
        } else {
            this.callBack();
        }
    }
    this._init = function () {
        if (this.find.length > 0) {
            this.trfor();
        } else {
            this.callbacks();
        }
    }
    this._init();
}