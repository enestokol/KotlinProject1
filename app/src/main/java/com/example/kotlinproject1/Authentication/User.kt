package com.example.kotlinproject1.Authentication

class User {
    var user_id:String?=null
    var username:String?=null
    var image:Int?=null
    var score:Long?=null

    constructor(user_id: String, username: String, image: Int, score: Long) {
        this.user_id = user_id
        this.username = username
        this.image = image
        this.score = score
    }

    constructor() {}
}


