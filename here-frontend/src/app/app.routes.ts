import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/components/infra/auth.guard';
import { HomeComponent } from './presentation/home/home.component';
import { LoginComponent } from './presentation/login/login.component';
import { UserFormComponent } from './presentation/user/user-form/user-form.component';
import { UserDetailComponent } from './presentation/user/user-detail/user-detail.component';
import { EventDetailComponent } from './presentation/event/event-detail/event-detail.component';
import { MyEventsComponent } from './presentation/event/my-events/my-events.component';

export const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'new-user', component: UserFormComponent },
  { path: 'user', component: UserDetailComponent, canActivate: [AuthGuard] },
  { path: 'event-detail/:id', component: EventDetailComponent, canActivate: [AuthGuard] },
  { path: 'my-events', component: MyEventsComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
