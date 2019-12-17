package com.example.kotlinproject1.Authentication

class User {
    var user_id:String?=null
    var username:String?=null
    var image:Int?=null
    var score:Long?=null
    var imageup:String?=null
    var highscore:Int?=null

    constructor(user_id: String, username: String, image: Int, score: Long,imageup:String?,highscore:Int?) {
        this.user_id = user_id
        this.username = username
        this.image = image
        this.score = score
        this.imageup=imageup
        this.highscore=highscore
    }

    constructor() {}
}


