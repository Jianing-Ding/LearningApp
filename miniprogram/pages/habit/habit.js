// miniprogram/pages/habit/habit.js
var util = require('../../utils.js');
Page({
  data: {
    TabCur: '0',
    scrollLeft: 0,
    date: null,
    calendarHide:true,
    year:0,
    month:0,
    clickDate: null,
    dayStyle: [
      { month: 'current', day: new Date().getDate(), color: 'white', background: '#AAD4F5' },
      { month: 'current', day: new Date().getDate(), color: 'white', background: '#AAD4F5' }
    ],
  },
  tabSelect(e) {
    console.log(e.currentTarget.dataset.id)
    this.setData({
      TabCur: e.currentTarget.dataset.id,
      scrollLeft: (e.currentTarget.dataset.id - 1) * 60,
    })
    /*const target = e.currentTarget.dataset.name;//1或者2得到点击了按钮1或者按钮2 
    const url = "/pages/habit/" + target+"/"+target;//得到页面url 
    wx.navigateTo({
      url: url,
    })*/
  },
  dayClick: function (event) {
    var year=event.detail.year;
    var month = event.detail.month;
    var day = event.detail.day;
    this.setData({
      date: year + "年" + month + "月" + day + "日",
      clickDate: this.data.date
    })
    let clickDay = event.detail.day;
    let changeDay = `dayStyle[1].day`;
    let changeBg = `dayStyle[1].background`;
    this.setData({
      [changeDay]: clickDay,
      [changeBg]: "#84e7d0"
    })
  },
  showCalendar(e){
      this.setData({
        calendarHide: !this.data.calendarHide
      }) 
  },
  onPostClick(e){
    wx.navigateTo({
      url: "/pages/habit/newHabits/newHabits"
    })
  },
  /**
   * 页面的初始数据
   */

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var time=new Date();
    //var time = util.formatTime(new Date());
   /* var timestamp = Date.parse(new Date());
    var date = new Date(timestamp);*/
    // 再通过setData更改Page()里面的data，动态更新页面的数据
    this.setData({
      date: time.getFullYear()+"年"+(time.getMonth()+1)+"月"+time.getDate()+"日",
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})
