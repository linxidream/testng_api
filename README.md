# testng_api
testng
moco接口配置：java -jar moco-runner-1.1.0-standalone.jar http -p 端口号 -c json文件
login：
[
  {
    "request": {
      "method":"post",
      "uri":"/api/login",
      "headers": {
        "content-type": "application/json"
      },
	  "json":{
		"account": "admin",
		"password": "admin"
	  }
	},
    "response":{
	   "json":{
		 "code":200,
		 "success":true,
		 "msg":"操作成功",
		 "data":{
			"id":"1",
			"accountName":"admin",
			"userName":"管理员",
			"userOrigin":"MC_CENTER",
			"token":"eyJhbGciOiJIUzI1NiIsIlR5cGUiOiJKd3QiLCJ0eXAiOiJKV1QifQ.eyJ1c2VyTmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTU5NDM1NDY3MiwidXNlcklkIjoxLCJhY2NvdW50IjoiYWRtaW4ifQ.3dVOQ8nt2LAoixyMXdpVeiCIk3akuJLWaxnKxrCcLbI",
			"needSetPd":null,
			"level":null
		 }
	   }	
	}
  }
]

add_user:
[
  {
    "request": {
      "method":"post",
      "uri":"/api/insert_user",
      "headers": {
        "content-type": "application/json"
      },
	  "json":{
		"account": "aaa",
		"password": "aaa"
	  }
	},
    "response":{
	   "json":{
		 "code":200,
		 "success":true,
		 "msg":"操作成功",
		 "data":{
			"user_id":"12",
			"accountName":"aaa",
			"userName":"aaa",
			"token":"eyJhbGciOiJIUzI1NiIsIlR5cGUiOiJKd3QiLCJ0eXAiOiJKV1QifQ.eyJ1c2VyTmFtZSI6IueuoeeQhuWRmCIsImV4cCI6MTU5NDM1NDY3MiwidXNlcklkIjoxLCJhY2NvdW50IjoiYWRtaW4ifQ.3dVOQ8nt2LAoixyMXdpVeiCIk3akuJLWaxnKxrCcLbI",
			"needSetPd":null,
			"level":null
		 }
	   }	
	}
  }
]
