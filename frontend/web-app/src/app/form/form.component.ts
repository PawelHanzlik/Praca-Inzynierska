import { Component, OnInit } from '@angular/core';
import {AppService} from "../app-service";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  cordX: cord;
  cordY: cord;
  disabledYes: form;
  disabledNo: form;
  paidYes: form;
  paidNo: form;
  guardedYes: form;
  guardedNo: form;
  freeSpacesYes: form;
  freeSpacesNo: form;
  forms: String[];
  previousX : number;
  previousY : number;
  constructor(private appService: AppService) {
    this.forms = []
    this.previousX = 0
    this.previousY = 0
    this.cordX = {
      value: 0
    }
    this.cordY = {
      value: 0
    }
    this.disabledYes = {
      value: "Yes"
    }
    this.disabledNo = {
      value: "No"
    }
    this.paidYes = {
      value: "Yes"
    }
    this.paidNo = {
      value: "No"
    }
    this.guardedYes = {
      value: "Yes"
    }
    this.guardedNo = {
      value: "No"
    }
    this.freeSpacesYes = {
      value: "Yes"
    }
    this.freeSpacesNo = {
      value: "No"
    }
  }

  ngOnInit(): void {
  }

  fooX(value: any) {
    this.cordX.value = value.value;
  }

  getPreviousValueX(value: any) {
    this.previousX = value.value;
  }
  fooY(value: any) {
    this.cordY.value = value.value;
  }

  getPreviousValueY(value: any) {
    this.previousY = value.value;
  }

  public solve(): void {
    if (this.disabledYes.value=="Yes"){
      this.forms.push(this.disabledYes.value)
    }
    else
      this.forms.push(this.disabledNo.value)

    if (this.paidYes.value=="Yes"){
      this.forms.push(this.paidYes.value)
    }
    else
      this.forms.push(this.paidNo.value)

    if (this.guardedYes.value=="Yes"){
      this.forms.push(this.guardedYes.value)
    }
    else
      this.forms.push(this.guardedNo.value)

    if (this.freeSpacesYes.value=="Yes"){
      this.forms.push(this.freeSpacesYes.value)
    }
    else
      this.forms.push(this.freeSpacesNo.value)

    console.log(this.cordY.value+" "+this.cordX.value)
    this.appService.solve(this.cordX.value,this.cordY.value,this.forms);
  }


}
interface form{
  value: String;
}
interface cord{
  value: number;
}
