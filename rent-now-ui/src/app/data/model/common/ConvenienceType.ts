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
//todo do wywalenia
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

export function getConvenienceTypeText(convenienceType: unknown): string {
  switch (convenienceType) {
    case 'PRIVATE_BATHROOM':
      return 'Private Bathroom';
    case 'BALCONY':
      return 'Balcony';
    case 'KITCHEN':
      return 'Kitchen';
    case 'AIR_CONDITIONING':
      return 'Air Conditioning';
    case 'POOL':
      return 'Pool';
    case 'TV':
      return 'TV';
    case 'BREAKFAST':
      return 'Breakfast';
    case 'WASHING_MACHINE':
      return 'Washing Machine';
    case 'DINNER':
      return 'Dinner';
    case 'SAUNA':
      return 'Sauna';
    case 'PARKING':
      return 'Parking';
    default:
      return 'Unknown';
  }
}
