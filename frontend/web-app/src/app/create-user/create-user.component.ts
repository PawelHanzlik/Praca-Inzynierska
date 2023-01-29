import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AppService } from '../app-service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.css']
})
export class CreateUserComponent implements OnInit {
    form!: FormGroup;

  constructor(private formBuilder: FormBuilder, private appService: AppService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
        name: [''],
        surname: [''],
        age: ['']
    })
  }

  onSubmit(): void {
    console.log(this.form.value);
    this.appService.createUser(this.form.value);
  }

}
