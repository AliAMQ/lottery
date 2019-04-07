import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LotteryUserProfileModule } from './user-profile/user-profile.module';
import { LotteryCategoryModule } from './category/category.module';
import { LotteryPrizeModule } from './prize/prize.module';
import { LotteryLotteryModule } from './lottery/lottery.module';
import { LotteryHistoryModule } from './history/history.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        LotteryUserProfileModule,
        LotteryCategoryModule,
        LotteryPrizeModule,
        LotteryLotteryModule,
        LotteryHistoryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LotteryEntityModule {}
