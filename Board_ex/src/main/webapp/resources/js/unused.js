/**
 * 
 */
//모든 게시글을 불러와주는 메소드
    var loadPosts = function() {
        $('.Items').empty();
        $.ajax({
            type:"post",
            url:'/stk/load',
            contentType: "application/json; charset=UTF-8",
            success:function(data) {
            	console.log(data);
            	//var data=JSON.parse(strdata);
                //alert(data.result);
                if(data.result=="success"){
                    var cnt = data.postlist.length;
                    for(var i=0; i<cnt;i++) {
                        var id=data.postlist[i].POST_ID;
                        var subject=data.postlist[i].POST_TITLE;
                        var content=data.postlist[i].POST_CONT;
                        var writer=data.postlist[i].POST_USERID;
                        var writedate=data.postlist[i].POST_REGDATE;
                        var moddate = data.postlist[i].POST_MODDATE;
                        var item=$('<div></div>').attr('data-id',id).addClass('Item');
                        var itemText=$('<div></div').addClass('ItemText').attr('writer',writer).appendTo(item);
                        $('<h4></h4>').text(subject).appendTo(itemText);
                        $('<h6></h6>').text('작성시간 : ' + writedate).appendTo(itemText);
                        $('<p></p>').text(content).appendTo(itemText);
                        if(writer==$.cookie('curuser')){
                            var itemButtons=$('<div></div>').addClass('ItemButtons').appendTo(itemText);
                            $('<button></button>').addClass('mainBtnDel BtnRed').text('삭제하기').appendTo(itemButtons);
                        }
                        var comment = $('<div></div>').addClass('Comment').appendTo(item);
                        $('<input />').attr({type:'text',placeholder:'댓글입력..'}).addClass('itemTxtComment').appendTo(comment);
                        $('<button></button>').addClass('commentBtnWrite BtnBlue').text('댓글달기').appendTo(comment);
    
                        $('<div></div>').addClass('Comments').appendTo(comment);
                        item.appendTo($('.Items'));
                        //loadComment(id);
                    }
                }else {
                    alert("오류발생");
                    $('.Main').hide();
                    $('.Login').show();
                }
            },
            error:function(err) {
                alert(err);
                alert('ajax 오류발생');
                $('.Main').hide();
                $('.Login').show();
            }
        });
    };
  //모든 게시글을 불러와주는 메소드
    var loadPosts = function() {
        $('.Items').empty();
        $.ajax({
            type:"post",
            url:'/stk/load',
            contentType: "application/json; charset=UTF-8",
            success:function(data) {
            	console.log(data);
            	//var data=JSON.parse(strdata);
                //alert(data.result);
                if(data.result=="success"){
                    var cnt = data.postlist.length;
                    for(var i=0; i<cnt;i++) {
                        var id=data.postlist[i].POST_ID;
                        var subject=data.postlist[i].POST_TITLE;
                        var content=data.postlist[i].POST_CONT;
                        var writer=data.postlist[i].POST_USERID;
                        var writedate=data.postlist[i].POST_REGDATE;
                        var moddate = data.postlist[i].POST_MODDATE;
                        var item=$('<div></div>').attr('data-id',id).addClass('Item');
                        var itemText=$('<div></div').addClass('ItemText').attr('writer',writer).appendTo(item);
                        $('<h4></h4>').text(subject).appendTo(itemText);
                        $('<h6></h6>').text('작성시간 : ' + writedate).appendTo(itemText);
                        $('<p></p>').text(content).appendTo(itemText);
                        if(writer==$.cookie('curuser')){
                            var itemButtons=$('<div></div>').addClass('ItemButtons').appendTo(itemText);
                            $('<button></button>').addClass('mainBtnDel BtnRed').text('삭제하기').appendTo(itemButtons);
                        }
                        var comment = $('<div></div>').addClass('Comment').appendTo(item);
                        $('<input />').attr({type:'text',placeholder:'댓글입력..'}).addClass('itemTxtComment').appendTo(comment);
                        $('<button></button>').addClass('commentBtnWrite BtnBlue').text('댓글달기').appendTo(comment);
    
                        $('<div></div>').addClass('Comments').appendTo(comment);
                        item.appendTo($('.Items'));
                        //loadComment(id);
                    }
                }else {
                    alert("오류발생");
                    $('.Main').hide();
                    $('.Login').show();
                }
            },
            error:function(err) {
                alert(err);
                alert('ajax 오류발생');
                $('.Main').hide();
                $('.Login').show();
            }
        });
    };
  //게시글 작성하기에서 게시글 작성하기(DB에 등록)
    var isPost=false;
    $('.writeBtnWrite').click(function() {
        if(isPost) return false;
        var subject=$('.writeTextSubject').val();
        var content=$('.writeTextContent').val();
        if(!subject) {
            alert("제목입력!");
            return false;
        } else if(!content) {
            alert("내용입력!");
            return false;
        }
        if(confirm("작성?")) {
            isPost=true;
            $.ajax({
                type:"post",
                url:"/stk/post",
                data: JSON.stringify({
                    post_title:subject,
                    post_cont : content,
                    post_userid: currentUser
                }),
                contentType: "application/json; charset=UTF-8",
                success:function(strdata) {
                	var data = JSON.parse(strdata);
                    if(data.result=="success") {
                        $('.Write').hide();
                        $('.Main').show();
                        loadPosts();
                    }else { alert("오류발생");}
                    isPost=false;
                },
                error:function(err) {
                    alert("ajax 연결문제");
                    console.log(err);
                    isPost=false;
                }
            });
        }

    });
    var isComment = false;
    $(document.body).on('click','.commentBtnWrite',function() {
        if(isComment) return false;
        var parentId=$(this).parent().parent().attr('data-id');
        var content=$(this).prev().val();
        var comments=$(this).next();
        console.log(parentId +"");
        console.log(content);
        console.log(comments);
        if(!content){ alert("댓글!"); return false;}
        isComment=true;
        $.ajax({
            type:"post",
            url:"/stk/commentPost",
            data:JSON.stringify({
                parentId:parentId,
                content:content,
                writer:currentUser
            }),
            contentType: "application/json; charset=UTF-8",
            success: function(strdata) {
            	var data = JSON.parse(strdata);
                if(data.result=="success"){
                    var lid=data.lastId;
                    var commentItem = $('<div></div>').addClass('CommentItem').attr('data-id',lid);
                    $('<h4></h4>').text(currentUser).appendTo(commentItem);
                    $('<p></p>').text(content).appendTo(commentItem);
                    $('<button></button>').addClass('BtnRed commentBtnDel').text('삭제').appendTo(commentItem);
                    commentItem.appendTo(comments);
                }else{ alert('오류발생');    }
                isComment=false;
            },error:function() {
                alert('ajax 연결문제');
                isComment=false;
            }
        });
    });


    //각 게시글의 댓글 불러오는 메소드
    var loadComment=function(postId){
        if(!postId) return false;
        var target= $('div.Item[data-id='+postId+'] .Comments');
        $.ajax({
            type:"post",
            url:"/stk/commentLoad",
            data:JSON.stringify({
                postId:postId
            }),
            contentType: "application/json; charset=UTF-8",
            success:function(strdata) {
            	var data = JSON.parse(strdata);
                if(data.result=="success"){
                    var cnt=data.data.length;
                    for(var i=0;i<cnt;i++) {
                        var id=data.data[i].cno;
                        var content=data.data[i].ccontent;
                        var writer=data.data[i].cwriter;
                        var commentItem = $('<div></div>').addClass('CommentItem').attr('data-id',id);
                        $('<h4></h4>').text(writer).appendTo(commentItem);
                        $('<p></p>').text(content).appendTo(commentItem);
                        if($('body > div > div.Main > div.Navi > div > p > b').text()==writer) {
                            $('<button></button>').addClass('AppBtnRed commentBtnDel').text('삭제').appendTo(commentItem);
                        }
                        commentItem.appendTo(target);
                    }   
                }else{ alert('오류발생');    }
            },error:function() {
                alert('ajax 연결문제');
            }
        });
    };

    //동적으로 생성된 게시글의 댓글 삭제하기 버튼에 이벤트 달아주기
    $(document.body).on('click','.commentBtnDel',function() {
        if(confirm('삭제?')) {
            var id=$(this).parent().attr('data-id');
            var removeTarget=$(this).parent();
            $.ajax({
                type:"post",
                url:"/stk/commentDel",
                data:JSON.stringify({
                    postId:id
                }),
                contentType: "application/json; charset=UTF-8",
                success:function(strdata) {
                	var data = JSON.parse(strdata);
                    if(data.result=="success") {
                        removeTarget.remove();
                    }else{ alert("오류발생"); }
                },
                error:function() {
                    alert("ajax오류 발생");
                }
            });
        }
    });