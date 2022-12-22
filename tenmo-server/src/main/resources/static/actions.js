
document.addEventListener('DOMContentLoaded', () => {
console.log('jwt: '+ jwt);
document.querySelector('#balance-nav-button').addEventListener('click', (event) => {
  event.preventDefault();
//
  const jwt = document.querySelector('#jwt-token').value;
  const url = event.target.getAttribute('/balance');

  axios.get(url, {
    headers: {
      'Authorization': `Bearer ${jwt}`,
    },
  })
  .then((response) => {
    const data = response.data;
    console.log('data: '+ JSON.stringify(data));
  })
  .catch((error) => {
        if(error.response){
        console.error(error.response.data);
        console.error(error.response.status);
        console.error(error.response.headers);
        } else if(error.request){
        console.error(error.request);
        }
  });
});

document.querySelector('#pending-nav-button').addEventListener('click', (event) => {
  event.preventDefault();

const jwt = document.querySelector('#jwt-token').value;
  const url = event.target.getAttribute('/pending');

  axios.get(url, {
    headers: {
      'Authorization': `Bearer ${jwt}`,
    },
  })
  .then((response) => {
      const data = response.data;
    })
    .catch((error) => {
      if(error.response){
      console.error(error.response.data);
      console.error(error.response.status);
      console.error(error.response.headers);
      } else if(error.request){
      console.error(error.request);
      }
  });
});

document.querySelector('#users-nav-button').addEventListener('click', (event) => {
  event.preventDefault();

const jwt = document.querySelector('#jwt-token').value;
  const url = event.target.getAttribute('/users');

  axios.get(url, {
    headers: {
      'Authorization': `Bearer ${jwt}`,
    },
  })
  .then((response) => {
      const data = response.data;
    })
    .catch((error) => {
      if(error.response){
      console.error(error.response.data);
      console.error(error.response.status);
      console.error(error.response.headers);
      } else if(error.request){
      console.error(error.request);
      }
  });
});

document.querySelector('#transfers-nav-button').addEventListener('click', (event) => {
  event.preventDefault();
//
const jwt = document.querySelector('#jwt-token').value;
  const url = event.target.getAttribute('/transfers');

  axios.get(url, {
    headers: {
      'Authorization': `Bearer ${jwt}`,
    },
  })
  .then((response) => {
       const data = response.data;
     })
     .catch((error) => {
       if(error.response){
       console.error(error.response.data);
       console.error(error.response.status);
       console.error(error.response.headers);
       } else if(error.request){
       console.error(error.request);
       }
  });
});

});