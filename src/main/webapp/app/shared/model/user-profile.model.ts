import { Moment } from 'moment';
import { ILottery } from 'app/shared/model//lottery.model';

export const enum State {
    AK = 'AK',
    AL = 'AL',
    AZ = 'AZ',
    AR = 'AR',
    CA = 'CA',
    CO = 'CO',
    CT = 'CT',
    DE = 'DE',
    FL = 'FL',
    GA = 'GA',
    HI = 'HI',
    ID = 'ID',
    IL = 'IL',
    IN = 'IN',
    IA = 'IA',
    KS = 'KS',
    KY = 'KY',
    LA = 'LA',
    ME = 'ME',
    MD = 'MD',
    MA = 'MA',
    MI = 'MI',
    MN = 'MN',
    MS = 'MS',
    MO = 'MO',
    MT = 'MT',
    NE = 'NE',
    NV = 'NV',
    NH = 'NH',
    NJ = 'NJ',
    NM = 'NM',
    NY = 'NY',
    NC = 'NC',
    ND = 'ND',
    OH = 'OH',
    OK = 'OK',
    OR = 'OR',
    PA = 'PA',
    RI = 'RI',
    SC = 'SC',
    SD = 'SD',
    TN = 'TN',
    TX = 'TX',
    UT = 'UT',
    VT = 'VT',
    VA = 'VA',
    WA = 'WA',
    WV = 'WV',
    WI = 'WI',
    WY = 'WY'
}

export interface IUserProfile {
    id?: number;
    state?: State;
    city?: string;
    address?: string;
    phone?: string;
    imagepath?: string;
    firstname?: string;
    lastname?: string;
    email?: string;
    username?: string;
    since?: Moment;
    userId?: number;
    lotteries?: ILottery[];
}

export class UserProfile implements IUserProfile {
    constructor(
        public id?: number,
        public state?: State,
        public city?: string,
        public address?: string,
        public phone?: string,
        public imagepath?: string,
        public firstname?: string,
        public lastname?: string,
        public email?: string,
        public username?: string,
        public since?: Moment,
        public userId?: number,
        public lotteries?: ILottery[]
    ) {}
}
