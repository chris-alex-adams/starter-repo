export default class CoinDataAPI {

  getCoins(pageNumber) {
    let data = {};
    fetch("https://coinmarketpoll.com/apidata//coins/" + pageNumber)
      .then(res => res.json())
      .then(
        (result) => {
          data = {
              isLoaded: true,
              items: result
            }
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          data = {
              isLoaded: false,
              error
            }
        }
      )

      return data;
  }
}
