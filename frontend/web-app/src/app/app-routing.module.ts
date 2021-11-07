import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CityComponent} from "./city/city.component";
import {FormComponent} from "./form/form.component";
import {MainPageComponent} from "./main-page/main-page.component";
import {ResultComponent} from "./result/result.component";

const routes: Routes =[
  {path: 'city', component: CityComponent},
  {path: 'form', component: FormComponent},
  {path: '', component: MainPageComponent},
  {path: 'result',component: ResultComponent}
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }
