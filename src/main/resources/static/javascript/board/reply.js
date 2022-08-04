function replyChk() {
    let content = document.getElementById('textarea');
    if ( content.value() < 4 ) {
        alert('댓글은 3글자 이상');
        content.select();
        return false;
    }

    return true;
}

function replyDelete(replynum, boardnum) {
    // console.log(replynum,boardnum);
    if(confirm("진짜 삭제?")) {
        location.href='rDelete?replynum=' + replynum + '&boardnum=' + boardnum;
        return;
    }
    
    alert("삭제 취소");
}