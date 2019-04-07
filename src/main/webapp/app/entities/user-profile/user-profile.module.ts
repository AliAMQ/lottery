import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LotterySharedModule } from 'app/shared';
import { LotteryAdminModule } from 'app/admin/admin.module';
import {
    UserProfileComponent,
    UserProfileDetailComponent,
    UserProfileUpdateComponent,
    UserProfileDeletePopupComponent,
    UserProfileDeleteDialogComponent,
    userProfileRoute,
    userProfilePopupRoute
} from './';

const ENTITY_STATES = [...userProfileRoute, ...userProfilePopupRoute];

@NgModule({
    imports: [LotterySharedModule, LotteryAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        UserProfileComponent,
        UserProfileDetailComponent,
        UserProfileUpdateComponent,
        UserProfileDeleteDialogComponent,
        UserProfileDeletePopupComponent
    ],
    entryComponents: [UserProfileComponent, UserProfileUpdateComponent, UserProfileDeleteDialogComponent, UserProfileDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LotteryUserProfileModule {}
