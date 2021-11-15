import { Component, OnInit } from '@angular/core';
import {AppService} from "../app-service";

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

  disabledYes: form;
  disabledNo: form;
  freeSpacesYes: form;
  freeSpacesNo: form;
  guardedYes: form;
  guardedNo: form;
  spotSizeYes: form;
  spotSizeNo: form;
  forms: String[];
  constructor(private appService: AppService) {
    this.forms = []
    this.disabledYes = {
      value: "Yes"
    }
    this.disabledNo = {
      value: "No"
    }
    this.freeSpacesYes = {
      value: "Yes"
    }
    this.freeSpacesNo = {
      value: "No"
    }
    this.guardedYes = {
      value: "Yes"
    }
    this.guardedNo = {
      value: "No"
    }
    this.spotSizeYes = {
      value: "Yes"
    }
    this.spotSizeNo = {
      value: "No"
    }
  }

  ngOnInit(): void {
  }

  public solve(): void {
    if (this.disabledYes.value=="Yes"){
      this.forms.push(this.disabledYes.value)
    }
    else
      this.forms.push(this.disabledNo.value)

    if (this.freeSpacesYes.value=="Yes"){
      this.forms.push(this.freeSpacesYes.value)
    }
    else
      this.forms.push(this.freeSpacesNo.value)

    if (this.guardedYes.value=="Yes"){
      this.forms.push(this.guardedYes.value)
    }
    else
      this.forms.push(this.guardedNo.value)

    if (this.spotSizeYes.value=="Yes"){
      this.forms.push(this.spotSizeYes.value)
    }
    else
      this.forms.push(this.spotSizeNo.value)

    this.appService.solve(this.forms);
  }


}
interface form{
  value: String;
}
