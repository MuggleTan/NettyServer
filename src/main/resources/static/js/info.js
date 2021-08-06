layui.use('table', function () {
    let $ = layui.jquery,
        table = layui.table,
        form = layui.form

    table.render({
        elem: '#infoList',
        id: 'infoList',
        url: '/info/list', //数据接口
        toolbar: "#toolbar",
        limit: 15,
        initSort: {
            field: 'id',
            type: 'desc'
        },
        skin: 'nob',
        page: {
            // layout: ['prev', 'page', 'next','count', 'limit', 'refresh', 'skip'],
            limits: [5, 10, 15, 20],
        },
        where: {
            sno: '',
            sip: '',
            type: '',
            success: ''
        },
        cols: [[//表头
            {
                checkbox: true,
                fixed: 'left'
            },
            {
                field: 'id',
                title: '编号',
                minWidth: 10
            },
            {
                field: 'sno',
                title: '学号',
            },
            {
                field: 'sip',
                title: '提交IP',
            },
            {
                field: 'rip',
                title: '实际IP',
            },
            {
                field: 'type',
                title: '类型',
                templet: function (d) {
                    if (d.type === 1)
                        return "UDP";
                    else if (d.type === 2)
                        return "TCP";
                }
            },
            // {
            //     field: 'success',
            //     title: 'IP地址是否正确',
            //     templet: function (d) {
            //         if (d.success === '1')
            //             return "正确";
            //         else if (d.success === '2')
            //             return "错误";
            //     }
            // },
            {
                field: 'subTime',
                title: '提交时间',
            }
        ]]
    });


    /*监听搜索按钮*/
    form.on('submit(searchBtn)', function (data) {
        table.reload('infoList', {
            url: '/info/list',
            where: {
                sno: data.field.sno,
                sip: data.field.sip,
                type: data.field.type,
                success: data.field.success
            },
            curr: 1
        });
        return false;
    });
    /*监听重置按钮*/
    form.on('submit(clearBtn)', function () {
        table.reload('infoList', {
            url: '/info/list',
            where: {
                sno: '',
                sip: '',
                type: '',
                success: ''
            },
            page: {
                curr: 1
            }
        });
    });

    // setInterval(function () {
    //     $("#reset").click();
    // }, 5000);

});


