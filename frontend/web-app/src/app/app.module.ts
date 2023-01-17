 import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormComponent } from './form/form.component';
import { CityComponent } from './city/city.component';
import { ResultComponent } from './result/result.component';
import { HeaderComponent } from './header/header.component';
import { MainPageComponent } from './main-page/main-page.component';
import { FooterComponent } from './footer/footer.component';
 import {HttpClientModule} from "@angular/common/http";
 import {FormsModule, ReactiveFormsModule} from "@angular/forms";
 import {AppService} from "./app-service";
import { CreateUserComponent } from './create-user/create-user.component';
import { SelectUserComponent } from './select-user/select-user.component';

@NgModule({
  declarations: [
    AppComponent,
    FormComponent,
    CityComponent,
    ResultComponent,
    HeaderComponent,
    MainPageComponent,
    FooterComponent,
    CreateUserComponent,
    SelectUserComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [AppService],
  bootstrap: [AppComponent]
})
export class AppModule { }
