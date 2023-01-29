import { Component, OnInit } from '@angular/core';
import {AppService, User} from "../app-service";

@Component({
  selector: 'app-select-user',
  templateUrl: './select-user.component.html',
  styleUrls: ['./select-user.component.css']
})
export class SelectUserComponent implements OnInit {

  users: User[] = [];

  constructor(public appService: AppService) { }

  ngOnInit(): void {
    console.log(this.appService.getUsers().subscribe(res => this.users = res))
  }

  saveUser(userId: number){
    localStorage.setItem("userId", String(userId))
  }

}
