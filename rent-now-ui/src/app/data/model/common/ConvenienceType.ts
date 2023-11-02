export enum ConvenienceType {
  PRIVATE_BATHROOM,
  BALCONY,
  KITCHEN,
  AIR_CONDITIONING,
  POOL,
  TV,
  BREAKFAST,
  WASHING_MACHINE,
  DINNER,
  SAUNA,
  PARKING
}

export function getConvenienceType(convenienceString: string): ConvenienceType {
  convenienceString = convenienceString.toUpperCase();

  switch (convenienceString) {
    case 'PRIVATE_BATHROOM':
      return ConvenienceType.PRIVATE_BATHROOM;
    case 'BALCONY':
      return ConvenienceType.BALCONY;
    case 'KITCHEN':
      return ConvenienceType.KITCHEN;
    case 'AIR_CONDITIONING':
      return ConvenienceType.AIR_CONDITIONING;
    case 'POOL':
      return ConvenienceType.POOL;
    case 'TV':
      return ConvenienceType.TV;
    case 'BREAKFAST':
      return ConvenienceType.BREAKFAST;
    case 'WASHING_MACHINE':
      return ConvenienceType.WASHING_MACHINE;
    case 'DINNER':
      return ConvenienceType.DINNER;
    case 'SAUNA':
      return ConvenienceType.SAUNA;
    case 'PARKING':
      return ConvenienceType.PARKING;
    default:
      throw new Error('Not found');
  }
}

export function getConvenienceTypeText(convenienceType: ConvenienceType): string {
  switch (convenienceType) {
    case ConvenienceType.PRIVATE_BATHROOM:
      return 'Private Bathroom';
    case ConvenienceType.BALCONY:
      return 'Balcony';
    case ConvenienceType.KITCHEN:
      return 'Kitchen';
    case ConvenienceType.AIR_CONDITIONING:
      return 'Air Conditioning';
    case ConvenienceType.POOL:
      return 'Pool';
    case ConvenienceType.TV:
      return 'TV';
    case ConvenienceType.BREAKFAST:
      return 'Breakfast';
    case ConvenienceType.WASHING_MACHINE:
      return 'Washing Machine';
    case ConvenienceType.DINNER:
      return 'Dinner';
    case ConvenienceType.SAUNA:
      return 'Sauna';
    case ConvenienceType.PARKING:
      return 'Parking';
    default:
      return 'Unknown';
  }
}
