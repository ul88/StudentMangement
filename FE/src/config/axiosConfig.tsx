import axios from "axios";

axios.defaults.baseURL="http://localhost:8080";
axios.defaults.headers.post['Content-Type'] = 'application/json';

axios.interceptors.response.use(
  (response) => {
    
    if(response.headers["access_token"] != undefined) localStorage.setItem("access_token", response.headers["access_token"]);
    if(response.headers["refresh_token"] != undefined) localStorage.setItem("refresh_token", response.headers["refresh_token"]);
    return response;
  },
  async (error) => {
    if(error.response?.status === 401 && !error.config._retry){
      error.config._retry = true
      error.config.headers["Refresh_Token"] = localStorage.getItem("refresh_token");
      console.log("retry값: ",error.config._retry);
      try{
        const response = await axios.request(error.config)
        return response;
      } catch (refreshError : any){
        if(refreshError.response?.status === 401){
          localStorage.removeItem("access_token");
          localStorage.removeItem("refresh_token");
          window.location.href="";
        }
        return Promise.reject(refreshError); 
      }
    }
    return Promise.reject(error);
  }
)

axios.interceptors.request.use(
  (config) => {
    config.headers["access_token"] = localStorage.getItem("access_token");
    return config;
  }
)

export default axios