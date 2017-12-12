layui.define(['layer', 'element'], function (exports) {

    var $ = layui.jquery
        , layer = layui.layer
        , element = layui.element
        , device = layui.device();

    !function (target, node, event) {

        var hide = function () {
            layer.closeAll('tips');
        }, stope = layui.stope;

        var defaults = {
            menu: [{
                text: "菜单一",
                callback: function (t) {}
            }, {
                text: "菜单二",
                callback: function (t) {}
            }],
            target: function (t) {}
        };

        var menuclick = function (options,target){
            //options = 传过来的menu等 target为用户元素
            $(document).on("click", ".ui-context-menu-item", function () {
                var e = event(this).index();
                layer.closeAll('tips');
                options.menu[e].callback && options.menu[e].callback(target,$(this));
            });
        }

        var othis = function (target, options) {
            ////console.log(target),//console.log(node),//console.log(event),//console.log(options);
            options = event.extend(!0, {}, defaults, options);
            $(target).on('contextmenu', function () {
                var vthis = $(this);
                options.target(vthis);

                i = '';
                layui.each(options.menu, function (index, item) {
                    i += '<li class="ui-context-menu-item"><a href="javascript:void(0);" ><span>' + item.text + '</span></a></li>';
                });

                html = '<ul id="contextmenu">' + i + '</ul>';
                layer.tips(html, target, {
                    tips: 1,
                    time: 0,
                    anim: 5,
                    fixed: true,
                    skin: "layui-box layui-layim-contextmenu",
                    success: function (layero, index) {

                        menuclick(options,vthis);

                        var stopmp = function (e) {
                            stope(e);
                        };
                        layero.off('mousedown', stopmp).on('mousedown', stopmp);
                    }
                });
                return false;
            });

            $(document).off('mousedown', hide).on('mousedown', hide);
        };
        event.fn.menu = function (options) {
            return new othis(this, options), this;
        };

    }(window, document, $);


    exports('contextmenu');
});