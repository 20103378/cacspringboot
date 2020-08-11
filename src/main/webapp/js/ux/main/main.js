$package('jeecg.main');
jeecg.main = function(){
	var tab_length=[];
	var tab_nameremove;
	return {
		treeSelect:function(){
			var _this = $(this);
			var title=_this.text();
			var url=_this.attr('href');
			jeecg.main.addTab(title,url);
			return false;
		},
		treeInit:function(){
			var  $items =  $('#tree-box').find(".menu-item");
			$items.bind('click',this.treeSelect);
		},
		addTab:function(_title,_url){
			var boxId = '#tab-box';
			if($(boxId).tabs('exists',_title) ){
				var tab = $(boxId).tabs('getTab',_title);
				var index = $(boxId).tabs('getTabIndex',tab);
				$(boxId).tabs('select',index);
				if(tab && tab.find('iframe').length > 0){
					 var _refresh_ifram = tab.find('iframe')[0];
				     _refresh_ifram.contentWindow.location.href=_url;
			    }
			}else{
				var _content ="<iframe scrolling='auto' frameborder='0' src='"+_url+"' style='width:100%; height:100%'></iframe>";
				if(tab_length.length>5){
					// if(tab_length[0]=="设备状态信息"){
					// 	tab_nameremove=tab_length.splice("1","1");
					// }else{
						tab_nameremove=tab_length.splice("0","1");
					// }
					$(boxId).tabs('close',tab_nameremove);
				};
				$(boxId).tabs('add',{
					    title:_title,
					    content:_content,
					    closable:true});
				tab_length.push(_title);
			}
		},
		addTab_closable:function(_title,_url){
			var boxId = '#tab-box';
			if($(boxId).tabs('exists',_title) ){
				var tab = $(boxId).tabs('getTab',_title);
				var index = $(boxId).tabs('getTabIndex',tab);
				$(boxId).tabs('select',index);
				if(tab && tab.find('iframe').length > 0){
					 var _refresh_ifram = tab.find('iframe')[0];
				     _refresh_ifram.contentWindow.location.href=_url;
			    }
			}else{
				var _content ="<iframe scrolling='auto' frameborder='0' src='"+_url+"' style='width:100%; height:100%'></iframe>";
				// if(tab_length.length>7){
				// 	if(tab_length[0]=="设备状态信息"){
				// 		tab_nameremove=tab_length.splice("1","1");
				// 	}else{
				// 		tab_nameremove=tab_length.splice("0","1");
				// 	}
				// 	$(boxId).tabs('close',tab_nameremove);
				// };
				$(boxId).tabs('add',{
					    title:_title,
					    content:_content,
					    closable:false});
				// tab_length.push(_title);
			}
		},
		menuHover:function(){
			//菜单鼠标进入效果
			$('.menu-item').hover(
				function () {
					$(this).stop().animate({ paddingLeft: "25px" }, 200,function(){
						$(this).addClass("orange");
					});
				},
				function () {
					$(this).stop().animate({ paddingLeft: "15px" },function(){
						$(this).removeClass("orange");
					});
				}
			);
		},
		modifyPwd:function(){
			var pwdForm = $("#pwdForm");
			if(pwdForm.form('validate')){
				var parentId =$('#search_parentId').val();
				$("#edit_parentId").val(parentId)
				jeecg.saveForm(pwdForm,function(data){
					$('#modify-pwd-win').dialog('close');
				    pwdForm.resetForm();
				});
			 }
		},
		init:function(){
			this.treeInit();
			this.menuHover();

			//修改密码绑定事件
			$('.modify-pwd-btn').click(function(){
				$('#modify-pwd-win').dialog('open');
			});
			$('#btn-pwd-close').click(function(){
				$('#modify-pwd-win').dialog('close');
			});
			$('#btn-pwd-submit').click(this.modifyPwd);

		}
	};
}();
$(function(){
	jeecg.main.init();
	
});