import { IScore } from 'app/shared/model//score.model';

export interface IAthlete {
  id?: number;
  athleteName?: string;
  registrationNumber?: string;
  athleteScores?: IScore[];
}

export const defaultValue: Readonly<IAthlete> = {};
