import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CityComponent} from "./city/city.component";
import { CreateUserComponent } from './create-user/create-user.component';
import {FormComponent} from "./form/form.component";
import {MainPageComponent} from "./main-page/main-page.component";
import {ResultComponent} from "./result/result.component";
import { SelectUserComponent } from './select-user/select-user.component';

const routes: Routes = [
  {path: 'city', component: CityComponent},
  {path: 'form', component: FormComponent},
  {path: '', component: MainPageComponent},
  {path: 'result',component: ResultComponent},
  {path: 'create', component: CreateUserComponent},
  {path: 'select', component: SelectUserComponent},
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
