
export default class CoinUtil {
   static formatMoney(amount, decimalCount = 2, decimal = ".", thousands = ",") {
   try {
      decimalCount = Math.abs(decimalCount);
      decimalCount = isNaN(decimalCount) ? 2 : decimalCount;

      if(amount < 1.0) {
        decimalCount = 6;
      }

      const negativeSign = amount < 0 ? "-" : "";

      let i = parseInt(amount = Math.abs(Number(amount) || 0).toFixed(decimalCount)).toString();
      let j = (i.length > 3) ? i.length % 3 : 0;

      return "$" + negativeSign + (j ? i.substr(0, j) + thousands : '') + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousands) + (decimalCount ? decimal + Math.abs(amount - i).toFixed(decimalCount).slice(2) : "");
      } catch (e) {
        console.log(e)
      }
   };

   static secondsToHms(num) {
    // 3154,000 seconds in a year
    // 86400 seconds in a day
    // 3600 seconds in a hour
    console.debug("num: " + num);
    num = Number(num);
    var y = Math.floor(num / 31536000);
    var d = Math.floor(num % 31536000 / 86400);
    var h = Math.floor(num % 86400 / 3600);
    var m = Math.floor(num % 3600 / 60);
    var s = Math.floor(num % 3600 % 60);

    var yDisplay = y > 0 ? y + (y === 1 ? " year, " : " years ") : "";
    var dDisplay = d > 0 ? d + (d === 1 ? " day, " : " days ") : "";
    var hDisplay = h > 0 ? h + (h === 1 ? " hour, " : " hours, ") : "";
    var mDisplay = m > 0 ? m + (m === 1 ? " minute, " : " minutes, ") : "";
    var sDisplay = s > 0 ? s + (s === 1 ? " second " : " seconds ") : "";

    if(y > 0) {
      return yDisplay;
    } else if(d > 0) {
      return dDisplay;
    } else if(h > 0) {
      return hDisplay + mDisplay;
    } else if(m > 0) {
      return mDisplay + sDisplay;
    } else {
      return sDisplay;
    }
    return yDisplay + dDisplay + hDisplay + mDisplay + sDisplay;
}
}
